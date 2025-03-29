package shared.hub.application.service;

import reactor.core.publisher.Mono;
import shared.hub.application.model.ApiResponse;
import shared.hub.application.model.response.IntrospectResponse;

public interface IdentityService {
    Mono<ApiResponse<IntrospectResponse>> introspect(String token);
}
