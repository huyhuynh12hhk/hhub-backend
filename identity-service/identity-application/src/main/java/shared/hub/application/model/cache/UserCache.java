package shared.hub.application.model.cache;

import lombok.Data;
import shared.hub.domain.model.entity.User;

@Data
public class UserCache {
    private Long version;
    private User user;

    public UserCache cloneFrom(User user) {
        this.user = user;
        return this;
    }

    public UserCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
