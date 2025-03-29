package shared.hub.application.service.impl;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;
import shared.hub.application.httpclient.IdentityClient;
import shared.hub.application.model.ApiResponse;
import shared.hub.application.model.request.IntrospectRequest;
import shared.hub.application.model.response.IntrospectResponse;
import shared.hub.application.service.IdentityService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityServiceImpl implements IdentityService {
    IdentityClient identityClient;

    @Override
    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(
                IntrospectRequest.builder().token(token).build());
    }
}
