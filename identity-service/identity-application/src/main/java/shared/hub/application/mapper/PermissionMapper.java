package shared.hub.application.mapper;

import org.mapstruct.Mapper;

import shared.hub.application.model.request.PermissionRequest;
import shared.hub.application.model.response.PermissionResponse;
import shared.hub.domain.model.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
