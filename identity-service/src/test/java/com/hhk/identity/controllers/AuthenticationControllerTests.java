package com.hhk.identity.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhk.identity.dtos.request.AuthenticationRequest;
import com.hhk.identity.dtos.request.IntrospectRequest;
import com.hhk.identity.dtos.response.AuthenticationResponse;
import com.hhk.identity.dtos.response.IntrospectResponse;
import com.hhk.identity.services.AuthenticationService;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    private IntrospectRequest introspectRequest;
    private IntrospectResponse introspectResponse;

    @BeforeEach
    public void init() {
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
    }

    @Test
    public void authenticate_returnToken() throws Exception {
        when(authenticationService.authenticate(ArgumentMatchers.any())).thenReturn(authenticationResponse);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.token").value(authenticationResponse.getToken()));
    }

    @Test
    public void introspect_valid() throws Exception {
        when(authenticationService.introspect(ArgumentMatchers.any())).thenReturn(introspectResponse);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/introspect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(introspectRequest)));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.valid").value(introspectResponse.isValid()));
    }
}
