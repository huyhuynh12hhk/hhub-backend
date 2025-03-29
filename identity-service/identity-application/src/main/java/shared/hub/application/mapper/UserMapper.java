package shared.hub.application.mapper;

import org.mapstruct.Mapper;

import shared.hub.application.model.request.CreateUserRequest;
import shared.hub.application.model.response.UserResponse;
import shared.hub.domain.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);
}
