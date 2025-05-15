package shared.hub.auth.redission;

public interface RedisDistributeUtil {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
