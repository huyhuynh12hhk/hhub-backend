package shared.hub.application.service.permission;

import java.util.List;

import shared.hub.application.model.request.PermissionRequest;
import shared.hub.application.model.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    void delete(String permission);
}
