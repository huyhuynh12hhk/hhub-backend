package shared.hub.auth.service;


import shared.hub.auth.model.cache.UserCache;

public interface UserCacheService {
    UserCache getUser(String userId, Long version);
}
