package com.hhk.identity.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hhk.identity.constants.Roles;
import com.hhk.identity.dtos.ApiResponse;
import com.hhk.identity.dtos.request.ChangePasswordRequest;
import com.hhk.identity.dtos.request.CreateUserRequest;
import com.hhk.identity.dtos.request.ProfileCreationRequest;
import com.hhk.identity.dtos.request.UpdateUserRolesRequest;
import com.hhk.identity.dtos.response.RoleResponse;
import com.hhk.identity.dtos.response.UserProfileResponse;
import com.hhk.identity.dtos.response.UserResponse;
import com.hhk.identity.entities.User;
import com.hhk.identity.mapper.ProfileMapper;
import com.hhk.identity.mapper.UserMapper;
import com.hhk.identity.repositories.RoleRepository;
import com.hhk.identity.repositories.UserRepository;
import com.hhk.identity.repositories.httpclient.ProfileClient;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProfileMapper profileMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private ProfileClient profileClient;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private UpdateUserRolesRequest updateUserRolesRequest;
    private ChangePasswordRequest changePasswordRequest;
    private UserResponse userResponse1;
    private UserResponse userResponse2;
    private User user;

    @BeforeEach
    public void init() {
        var adminRole = RoleResponse.builder()
                .name(Roles.ADMIN_ROLE)
                .description("Admin role")
                .build();

        var userRole = RoleResponse.builder()
                .name(Roles.USER_ROLE)
                .description("User role")
                .build();
        var roles = new HashSet<RoleResponse>();
        roles.add(userRole);
        createUserRequest = CreateUserRequest.builder()
                .username("user01")
                .password("password")
                .fullName("John Doe")
                .email("JohnDoe01@gmew.com")
                .bio("This is user John Doe.")
                .build();

        changePasswordRequest = ChangePasswordRequest.builder()
                .oldPassword("password")
                .newPassword("password1")
                .confirmPassword("password1")
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

        user = User.builder()
                .id("37548f73-85c8-45e5-b69b-dd16c35e1054")
                .username("user01")
                .password("password")
                .email("JohnDoe01@gmew.com")
                .build();
    }

    @Test
    void createUser_success() {
        ProfileCreationRequest dummyProfileRequest = new ProfileCreationRequest();
        ApiResponse<UserProfileResponse> dummyProfileResponse = ApiResponse.<UserProfileResponse>builder()
                .data(UserProfileResponse.builder().uid(user.getId()).build())
                .build();
        when(userMapper.toUser(any(CreateUserRequest.class))).thenReturn(user);
        when(userMapper.toUserResponse(any())).thenReturn(userResponse1);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(profileMapper.toProfileCreationRequest(any())).thenReturn(dummyProfileRequest);
        when(profileClient.createProfile(any())).thenReturn(dummyProfileResponse);
        when(userRepository.save(any())).thenReturn(user);

        var response = userService.createUser(createUserRequest);

        Assertions.assertThat(response.getId()).isEqualTo("37548f73-85c8-45e5-b69b-dd16c35e1054");
        Assertions.assertThat(response.getUsername()).isEqualTo("user01");
    }

    @Test
    void changeUserPassword_success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getId());

        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toUserResponse(any())).thenReturn(userResponse1);
        when(userRepository.save(any())).thenReturn(user);

        var response = userService.changeUserPassword(changePasswordRequest);

        Assertions.assertThat(response.getId()).isEqualTo("37548f73-85c8-45e5-b69b-dd16c35e1054");
        Assertions.assertThat(response.getUsername()).isEqualTo("user01");
    }

    @Test
    void getMyInfo_success() {
        // authenticate
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getId());

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(any())).thenReturn(userResponse1);

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getId()).isEqualTo("37548f73-85c8-45e5-b69b-dd16c35e1054");
        Assertions.assertThat(response.getUsername()).isEqualTo("user01");
    }
}
