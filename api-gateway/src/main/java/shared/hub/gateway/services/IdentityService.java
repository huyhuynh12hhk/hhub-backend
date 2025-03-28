package shared.hub.gateway.services;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;
import shared.hub.gateway.dtos.ApiResponse;
import shared.hub.gateway.dtos.request.IntrospectRequest;
import shared.hub.gateway.dtos.response.IntrospectResponse;
import shared.hub.gateway.repositories.IdentityClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(
                IntrospectRequest.builder().token(token).build());
    }
}
