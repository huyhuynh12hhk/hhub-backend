package shared.hub.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import shared.hub.auth.dto.request.CreateUserRequest;
import shared.hub.auth.dto.request.RegisterUserRequest;
import shared.hub.auth.dto.response.ApiResponse;
import shared.hub.auth.dto.response.UserResponse;

public interface AppUserService {

    ResponseEntity<ApiResponse<UserResponse>> createUser(CreateUserRequest request);

    ResponseEntity<ApiResponse<UserResponse>> registerUser(RegisterUserRequest request);

    ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(Pageable pageable);

    ResponseEntity<ApiResponse<UserResponse>> getUser(String username);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
}
