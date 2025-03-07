package com.hhk.gateway.services;

import org.springframework.stereotype.Service;

import com.hhk.gateway.dtos.ApiResponse;
import com.hhk.gateway.dtos.request.IntrospectRequest;
import com.hhk.gateway.dtos.response.IntrospectResponse;
import com.hhk.gateway.repositories.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

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
