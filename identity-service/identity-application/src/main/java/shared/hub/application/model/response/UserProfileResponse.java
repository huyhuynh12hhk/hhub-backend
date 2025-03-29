package shared.hub.application.model.response;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String uid;
    String username;
    String profilePicture;
    String profileCover;
    String email;
    String fullName;
    String bio;
    boolean isActive;
    Instant dateJoined;
}
