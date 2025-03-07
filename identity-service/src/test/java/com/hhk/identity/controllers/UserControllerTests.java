package com.hhk.identity.controllers;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

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
import com.hhk.identity.constants.Roles;
import com.hhk.identity.dtos.request.CreateUserRequest;
import com.hhk.identity.dtos.request.UpdateUserRolesRequest;
import com.hhk.identity.dtos.response.RoleResponse;
import com.hhk.identity.dtos.response.UserResponse;
import com.hhk.identity.services.UserService;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest createUserRequest;
    private UpdateUserRolesRequest updateUserRolesRequest;
    private UserResponse userResponse1;
    private UserResponse userResponse2;

    @BeforeEach
    public void init() {
        var admin = RoleResponse.builder()
                .name(Roles.USER_ROLE)
                .description("User role")
                .build();
        var roles = new HashSet<RoleResponse>();
        roles.add(admin);
        createUserRequest = CreateUserRequest.builder()
                .username("user01")
                .password("password")
                .fullName("John Doe")
                .email("JohnDoe01@gmew.com")
                .bio("This is user John Doe.")
                .build();

        updateUserRolesRequest =
                UpdateUserRolesRequest.builder().roles(List.of("ADMIN")).build();

        userResponse1 = UserResponse.builder()
                .id("37548f73-85c8-45e5-b69b-dd16c35e1054")
                .username("user01")
                .email("JohnDoe01@gmew.com")
                .emailVerified(true)
                .roles(roles)
                .build();

        userResponse2 = UserResponse.builder()
                .id("88758f73-c5c8-45e5-268b-dd16c35e1054")
                .username("user02")
                .email("JohnDoe02@gmew.com")
                .emailVerified(true)
                .roles(roles)
                .build();
    }

    @Test
    void getUsers_success() throws Exception {
        var list = List.of(userResponse1, userResponse2);

        when(userService.getUsers()).thenReturn(list);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/users"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(2));
    }

    @Test
    void getUser_success() throws Exception {
        var id = userResponse1.getId();
        when(userService.getUser(id)).thenReturn(userResponse1);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + id));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.id").value(userResponse1.getId()));
    }

    @Test
    void createUser_success() throws Exception {
        when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse1);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").value(userResponse1.getUsername()));
    }

    @Test
    void updateUserRoles_success() throws Exception {
        when(userService.updateUserRoles(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(userResponse1);

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userResponse1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRolesRequest)));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").value(userResponse1.getUsername()));
    }

    @Test
    void deleteUser_success() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userResponse1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRolesRequest)));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data").value("User has been deleted"));
    }
}
