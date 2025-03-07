package com.hhk.identity.mapper;

import org.mapstruct.Mapper;

import com.hhk.identity.dtos.request.CreateUserRequest;
import com.hhk.identity.dtos.response.UserResponse;
import com.hhk.identity.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);
}
