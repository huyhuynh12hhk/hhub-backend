package shared.hub.auth.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String image;
    private String createdDate;
    private boolean active;
}
