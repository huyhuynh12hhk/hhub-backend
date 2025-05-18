package cache

import (
	"github.com/alicebob/miniredis/v2"
	"github.com/redis/go-redis/v9"
)

func mockRedis() *miniredis.Miniredis {
	s, err := miniredis.Run()

	if err != nil {
		panic(err)
	}

	return s
}

func NewRedisMock() *RedisClient {
	redisServer := mockRedis()
	client := redis.NewClient(&redis.Options{
		Addr: redisServer.Addr(),
	})
	return &RedisClient{
		client: client,
	}
}
