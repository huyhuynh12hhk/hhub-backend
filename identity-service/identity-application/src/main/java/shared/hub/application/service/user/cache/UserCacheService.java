package shared.hub.application.service.user.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import shared.hub.application.exception.AppException;
import shared.hub.application.exception.ErrorCode;
import shared.hub.application.model.cache.UserCache;
import shared.hub.infrastructure.cache.RedisCacheService;
import shared.hub.infrastructure.distributed.redission.RedisDistributeUtil;
import shared.hub.infrastructure.distributed.redission.RedisDistributedLocker;
import shared.hub.infrastructure.persistence.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserCacheService {
    RedisCacheService redisCacheService;
    RedisDistributeUtil redisDistributeUtil;
    UserRepository userRepository;

    private static final Cache<String, UserCache> userLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public UserCache getUser(String userId, Long version) {
        UserCache userCache = getUserLocalCache(userId);
        if (userCache != null) {
            // If version time <= cache issued time
            if (version == null || version <= userCache.getVersion()) {
                return userCache;
            }
        }
        // Others case
        return getDistributedCacheUser(userId);
    }

    private UserCache getUserLocalCache(String userId) {
        return userLocalCache.getIfPresent(userId);
    }

    private UserCache getDistributedCacheUser(String userId) {
        // Get from cache
        var userCache = redisCacheService.getObject(getItemKeyOf(userId), UserCache.class);
        // Not found -> get from database and set one
        if (userCache == null) {
            log.info("User {} not found, get from database", userId);
            userCache = getDatabaseUser(userId);
        }
        // Put to local cache
        userLocalCache.put(userId, userCache);

        return userCache;
    }

    private UserCache getDatabaseUser(String userId) {
        RedisDistributedLocker locker = redisDistributeUtil.getDistributedLock(getLockKeyOf(userId));
        try {
            // Start lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return null;
            }

            // Get cache
            var key = getItemKeyOf(userId);
            var userCache = redisCacheService.getObject(key, UserCache.class);
            if (userCache != null) {
                return userCache;
            }

            // If not cached
            var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Exist"));
            userCache = new UserCache().cloneFrom(user).withVersion(System.currentTimeMillis());
            redisCacheService.setObject(key, userCache);
            return userCache;
        } catch (Exception e) {
            log.error("Cache User got exception: {}", e);
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        } finally {
            locker.unlock();
        }
    }

    private String getLockKeyOf(String id) {
        return "IDENTITY:USER_LOCK" + id;
    }

    private String getItemKeyOf(String id) {
        return "IDENTITY:USER:" + id;
    }
}
