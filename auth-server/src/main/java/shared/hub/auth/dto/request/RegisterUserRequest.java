package shared.hub.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must 3-20 characters")
    private String username;
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 4, message = "Full name must be at least 4 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Full name must be alphabet characters")
    private String fullName;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
