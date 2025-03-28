package shared.hub.application.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String uid;
    String username;
    String email;
    String fullName;
    //    Byte[] profilePicture;
    //    Byte[] profileCover;
    String bio;
}
