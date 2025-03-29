package shared.hub.application.httpclient;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import reactor.core.publisher.Mono;
import shared.hub.application.model.ApiResponse;
import shared.hub.application.model.request.IntrospectRequest;
import shared.hub.application.model.response.IntrospectResponse;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
