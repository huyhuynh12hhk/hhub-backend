package shared.hub.gateway.repositories;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import reactor.core.publisher.Mono;
import shared.hub.gateway.dtos.ApiResponse;
import shared.hub.gateway.dtos.request.IntrospectRequest;
import shared.hub.gateway.dtos.response.IntrospectResponse;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
