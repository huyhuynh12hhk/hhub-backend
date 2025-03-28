package shared.hub.application.service.role;

import java.util.List;

import shared.hub.application.model.request.RoleRequest;
import shared.hub.application.model.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    void delete(String role);
}
