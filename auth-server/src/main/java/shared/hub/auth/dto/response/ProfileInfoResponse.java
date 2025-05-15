package shared.hub.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProfileInfoResponse {
    private String id;
    private String uid;
    private String username;
    private String fullName;
    private String email;
    private String profilePicture;
    private String profileCover;
    private String bio;
//    private boolean isActive;
    private Instant dateJoined;
}
