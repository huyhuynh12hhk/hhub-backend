package shared.hub.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import shared.hub.application.model.request.RoleRequest;
import shared.hub.application.model.response.RoleResponse;
import shared.hub.domain.model.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
