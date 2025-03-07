package com.hhk.identity.mapper;

import org.mapstruct.Mapper;

import com.hhk.identity.dtos.request.CreateUserRequest;
import com.hhk.identity.dtos.request.ProfileCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(CreateUserRequest request);
}
