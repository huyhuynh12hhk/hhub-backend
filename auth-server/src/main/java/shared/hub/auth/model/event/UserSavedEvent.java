package shared.hub.auth.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedEvent {
    private String username;
    private String email;
    private String image;
    private String createdDate;
    private boolean active;
}
