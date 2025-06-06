package shared.hub.auth.mapper;

import org.springframework.beans.BeanUtils;
import shared.hub.auth.dto.request.CreateUserRequest;
import shared.hub.auth.dto.request.RegisterUserRequest;
import shared.hub.auth.dto.request.SaveUserToElasticRequest;
import shared.hub.auth.dto.response.UserResponse;
import shared.hub.auth.model.entity.AppUser;
import shared.hub.auth.model.event.UserSavedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserMapper {
    public static UserResponse mapToUserResponse(AppUser user) {
        if (Objects.isNull(user)) return null;
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
//        response.setCreatedDate(user.getCreatedDate().toString());
        return response;
    }

    public static UserSavedEvent mapToSavedUserEvent(AppUser user) {
        if (Objects.isNull(user)) return null;
        UserSavedEvent event = new UserSavedEvent();
        BeanUtils.copyProperties(user, event);
        event.setCreatedDate(user.getCreatedDate().toString());
        return event;
    }

    public static SaveUserToElasticRequest mapToUserSearchDoc(UserSavedEvent event) {
        if (Objects.isNull(event)) return null;
        SaveUserToElasticRequest request = new SaveUserToElasticRequest();
        BeanUtils.copyProperties(event, request);
        return request;
    }


    public static List<UserResponse> mapToListUserResponse(List<AppUser> users) {
        List<UserResponse> response = new ArrayList<>();
        for (AppUser user : users) {
            response.add(mapToUserResponse(user));
        }
        return response;
    }

    public static AppUser mapToUserEntity(RegisterUserRequest request) {
        if (Objects.isNull(request)) return null;
        return AppUser.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .fullName(request.getFullName())
                .active(true)
                .verified(false)
                .image("")
                .build();
    }

    public static AppUser mapToUserEntity(CreateUserRequest request) {
        if (Objects.isNull(request)) return null;
        return AppUser.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .active(true)
                .verified(false)
                .image("")
                .build();

    }
}
