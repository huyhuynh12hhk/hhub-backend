package shared.hub.application.service.user.impl;

// import com.hhk.event.dto.NotificationEvent;

import java.util.HashSet;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import shared.hub.application.exception.AppException;
import shared.hub.application.exception.ErrorCode;
import shared.hub.application.mapper.ProfileMapper;
import shared.hub.application.mapper.UserMapper;
import shared.hub.application.model.request.ChangePasswordRequest;
import shared.hub.application.model.request.CreateUserRequest;
import shared.hub.application.model.request.UpdateUserRolesRequest;
import shared.hub.application.model.response.UserResponse;
import shared.hub.application.service.user.UserService;
import shared.hub.domain.model.constant.Roles;
import shared.hub.domain.model.entity.Role;
import shared.hub.domain.model.entity.User;
import shared.hub.infrastructure.persistence.repository.RoleRepository;
import shared.hub.infrastructure.persistence.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    //    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(Roles.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
            var profileRequest = profileMapper.toProfileCreationRequest(request);
            profileRequest.setUid(user.getId());
            profileRequest.setBio("");
            profileRequest.setEmail("");
            profileRequest.setFullName(request.getUsername());

            //            profileClient.createProfile(profileRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } catch (Exception exception) {
            log.info("Create profile error: " + exception);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        //        NotificationEvent notificationEvent = NotificationEvent.builder()
        //                .channel("EMAIL")
        //                .recipient(request.getEmail())
        //                .subject("Welcome to bookside")
        //                .body("Hello, " + request.getUsername())
        //                .build();

        // Publish message to kafka
        //        kafkaTemplate.send("notification-delivery", notificationEvent);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUserRoles(String userId, UpdateUserRolesRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse changeUserPassword(ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
