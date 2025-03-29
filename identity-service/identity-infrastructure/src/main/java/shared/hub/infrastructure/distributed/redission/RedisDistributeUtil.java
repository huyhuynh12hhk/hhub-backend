package shared.hub.infrastructure.distributed.redission;

public interface RedisDistributeUtil {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
