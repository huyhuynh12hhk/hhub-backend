package com.hhk.identity.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.hhk.identity.dtos.request.AuthenticationRequest;
import com.hhk.identity.dtos.request.IntrospectRequest;
import com.hhk.identity.dtos.response.AuthenticationResponse;
import com.hhk.identity.dtos.response.IntrospectResponse;
import com.hhk.identity.entities.User;
import com.hhk.identity.repositories.InvalidatedTokenRepository;
import com.hhk.identity.repositories.UserRepository;
import com.nimbusds.jose.JWSObject;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    private IntrospectRequest introspectRequest;
    private IntrospectResponse introspectResponse;
    private User user;

    @BeforeEach
    public void init() {

        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY", "<some_secret_key_with_right_format>");
        ReflectionTestUtils.setField(authenticationService, "VALID_DURATION", 3600);
        ReflectionTestUtils.setField(authenticationService, "REFRESHABLE_DURATION", 36000);

        authenticationRequest = AuthenticationRequest.builder()
                .username("user01")
                .password("password")
                .build();
        authenticationResponse = AuthenticationResponse.builder()
                .token("<a_valid_and_very_strong_jwt_token>")
                .build();
        introspectRequest = IntrospectRequest.builder()
                .token("<a_valid_and_very_strong_jwt_token>")
                .build();
        introspectResponse = IntrospectResponse.builder().valid(true).build();
        user = User.builder()
                .username("user01")
                .password("password")
                .email("JohnDoe01@gmew.com")
                .build();
    }

    @Test
    void authenticated_success() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        try (MockedConstruction<BCryptPasswordEncoder> encoder =
                        mockConstruction(BCryptPasswordEncoder.class, (mock, context) -> {
                            when(mock.matches(anyString(), anyString())).thenReturn(true);
                        });
                MockedConstruction<JWSObject> mocked = mockConstruction(JWSObject.class, (mock, context) -> {
                    doNothing().when(mock).sign(any());
                    when(mock.serialize()).thenReturn(authenticationResponse.getToken());
                })) {
            var result = authenticationService.authenticate(authenticationRequest);

            Assertions.assertThat(result).isEqualTo(authenticationResponse);
        }
    }
}
