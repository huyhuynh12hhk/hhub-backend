package com.hhk.identity.mapper;

import org.mapstruct.Mapper;

import com.hhk.identity.dtos.request.PermissionRequest;
import com.hhk.identity.dtos.response.PermissionResponse;
import com.hhk.identity.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
