package cache

import (
	"context"
	"encoding/json"
	"fmt"
	"hhub/connection-service/global"
	"time"

	"github.com/go-redsync/redsync/v4"
	"github.com/go-redsync/redsync/v4/redis/goredis/v9"
	"github.com/redis/go-redis/v9"
)

type RedisClient struct {
	client *redis.Client
}

func NewRedis() *RedisClient {

	redisConf := global.Config.Redis
	rUrl := fmt.Sprintf("redis://:%s@%s/%d", redisConf.Password, redisConf.Addr, 0)
	opt, err := redis.ParseURL(rUrl)
	if err != nil {
		panic(err)
	}
	// opt.DialTimeout = 100 * time.Millisecond
	// opt.ReadTimeout = 100 * time.Millisecond

	client := redis.NewClient(opt)

	if _, err := client.Ping(context.Background()).Result(); err != nil {
		fmt.Printf("Ping error %+v\n", err)
		panic("fail to initialize redis client, please check connection address again")
	}

	return &RedisClient{
		client: client,
	}

}

func (c *RedisClient) GetValueWithLock(key string) (interface{}, error) {
	pool := goredis.NewPool(c.client)

	rs := redsync.New(pool)

	ctx := context.Background()
	mutex := rs.NewMutex(key + "_redsync_lock")

	if err := mutex.Lock(); err != nil {
		panic(err)
	}

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		panic(err)
	}

	var data map[string]interface{}
	if err = json.Unmarshal([]byte(val), &data); err != nil {
		panic(err)
	}

	if _, err = mutex.Unlock(); err != nil {
		panic(err)
	}

	return data, nil
}

func (c *RedisClient) SetValueWithLock(key string, value interface{}, expiration time.Duration) error {
	valStr, err := json.Marshal(value)
	if err != nil {
		panic(err)
	}
	pool := goredis.NewPool(c.client)

	rs := redsync.New(pool)

	ctx := context.Background()
	mutex := rs.NewMutex(key + "_redsync_lock")

	if err := mutex.Lock(); err != nil {
		panic(err)
	}

	err = c.client.Set(ctx, key, valStr, expiration).Err()
	if err != nil {
		panic(err)
	}

	if _, err = mutex.Unlock(); err != nil {
		panic(err)
	}

	return nil
}

func GetOrSetValues[T any](
	client *RedisClient,
	key string,
	callback func() []T,
	expiration time.Duration,
) ([]T, error) {
	ctx := context.Background()
	var items []T

	if data, err := client.client.Get(ctx, key).Result(); err == nil {
		if err := json.Unmarshal([]byte(data), &items); err == nil {
			fmt.Println("Got cached data")
			return items, nil
		}
	}

	items = callback()

	if err := client.SetValueWithLock(key, items, expiration); err != nil {
		return nil, err
	}
	fmt.Println("Cache not found, set new cache")

	return items, nil
}
