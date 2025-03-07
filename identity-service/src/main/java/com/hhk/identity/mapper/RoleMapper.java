package com.hhk.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hhk.identity.dtos.request.RoleRequest;
import com.hhk.identity.dtos.response.RoleResponse;
import com.hhk.identity.entities.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
