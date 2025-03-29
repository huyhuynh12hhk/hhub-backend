package shared.hub.application.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import shared.hub.application.model.request.ChangePasswordRequest;
import shared.hub.application.model.request.CreateUserRequest;
import shared.hub.application.model.request.UpdateUserRolesRequest;
import shared.hub.application.model.response.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getMyInfo();

    UserResponse updateUserRoles(String userId, UpdateUserRolesRequest request);

    UserResponse changeUserPassword(ChangePasswordRequest request);

    void deleteUser(String userId);

    public Page<UserResponse> getUsers(Pageable pageable);

    public UserResponse getUser(String id, Long version);
}
