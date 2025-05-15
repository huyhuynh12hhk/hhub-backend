package shared.hub.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shared.hub.auth.constant.Roles;
import shared.hub.auth.dto.request.CreateUserRequest;
import shared.hub.auth.dto.request.ProfileCreationRequest;
import shared.hub.auth.dto.request.RegisterUserRequest;
import shared.hub.auth.dto.response.ApiResponse;
import shared.hub.auth.dto.response.UserResponse;
import shared.hub.auth.exception.AppException;
import shared.hub.auth.exception.NotifyCode;
import shared.hub.auth.mapper.UserMapper;
import shared.hub.auth.model.entity.AppUser;
import shared.hub.auth.model.entity.Role;
import shared.hub.auth.repository.AppUserRepository;
import shared.hub.auth.repository.ProfileHttpClient;
import shared.hub.auth.repository.RoleRepository;
import shared.hub.auth.service.AppUserService;
import shared.hub.auth.service.UserCacheService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheService userCacheService;
    private final ProfileHttpClient profileHttpClient;

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> createUser(CreateUserRequest request) {
        AppUser appUser = UserMapper.mapToUserEntity(request);

        if (appUser == null) throw new AppException(NotifyCode.INVALID_PARAM);
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new AppException(NotifyCode.USERNAME_HAS_EXISTED);
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new AppException(NotifyCode.EMAIL_HAS_EXISTED);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        request.getRoles().forEach(r -> {
            Role role = roleRepository.findByName(Roles.valueOf(r))
                    .orElseThrow(() -> new RuntimeException("role not found"));
            roles.add(role);
        });
        appUser.setRoles(roles);
        userRepository.save(appUser);

        profileHttpClient.createProfile(
                ProfileCreationRequest.builder()
                        .email(appUser.getEmail())
                        .uid(appUser.getId())
                        .fullName(request.getFullName())
                        .build()
        );

        var notiCode = NotifyCode.CREATED;
        var response = ApiResponse.success(UserMapper.mapToUserResponse(appUser), notiCode);

        return ResponseEntity
                .status(notiCode.getStatusCode())
                .body(response);
    }

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(RegisterUserRequest request) {
        AppUser appUser = UserMapper.mapToUserEntity(request);
        if (appUser == null) throw new AppException(NotifyCode.INVALID_PARAM);
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new AppException(NotifyCode.USERNAME_HAS_EXISTED);
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new AppException(NotifyCode.EMAIL_HAS_EXISTED);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new AppException(NotifyCode.INTERNAL_SERVER_ERROR));
        appUser.setRoles(new HashSet<>(List.of(role)));
        userRepository.save(appUser);

//        var profile = ProfileCreationRequest.builder()
//                .email(appUser.getEmail())
//                .uid(appUser.getId())
//                .username(appUser.getUsername())
//                .fullName(appUser.getFullName())
//                .build();

//                profile
        try {
            profileHttpClient.createProfile(
                    ProfileCreationRequest.builder()
                            .uid(appUser.getId())
                            .email(appUser.getEmail())
                            .username(appUser.getUsername())
                            .fullName(request.getFullName())
                            .build()
            );
        } catch (Exception e) {
            log.error("Fail to call profile service.");
        }


        var notiCode = NotifyCode.CREATED;
        var response = ApiResponse.success(UserMapper.mapToUserResponse(appUser), notiCode);

        return ResponseEntity
                .status(notiCode.getStatusCode())
                .body(response);

    }

    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(Pageable pageable) {

        var rs = userRepository.findAll(pageable).map(UserMapper::mapToUserResponse);
        var notiCode = NotifyCode.SUCCESS;

        return ResponseEntity
                .status(notiCode.getStatusCode())
                .body(ApiResponse.success(rs, notiCode));
    }

    public ResponseEntity<ApiResponse<UserResponse>> getUser(String username) {

        var rs = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(NotifyCode.NOT_FOUND));
        var notiCode = NotifyCode.SUCCESS;

        return ResponseEntity
                .status(notiCode.getStatusCode())
                .body(ApiResponse.success(UserMapper.mapToUserResponse(rs), notiCode));
    }

    public boolean checkUsernameExists(String username) {
        return !Objects.isNull(userRepository.findByUsername(username));
    }

    public boolean checkEmailExists(String email) {
        return !Objects.isNull(userRepository.findByEmail(email));
    }

}
