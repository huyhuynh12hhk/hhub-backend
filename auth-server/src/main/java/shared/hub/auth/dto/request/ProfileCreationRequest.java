package shared.hub.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileCreationRequest {
    private String uid;
    private String username;
    private String email;
    private String fullName;
    @Builder.Default
    private String profilePicture = "";
    @Builder.Default
    private String profileCover = "";
    @Builder.Default
    private String bio = "";
}
