package com.hhk.identity.services;

// import com.hhk.event.dto.NotificationEvent;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hhk.identity.constants.Roles;
import com.hhk.identity.dtos.request.ChangePasswordRequest;
import com.hhk.identity.dtos.request.CreateUserRequest;
import com.hhk.identity.dtos.request.UpdateUserRolesRequest;
import com.hhk.identity.dtos.response.UserResponse;
import com.hhk.identity.entities.Role;
import com.hhk.identity.entities.User;
import com.hhk.identity.exceptions.AppException;
import com.hhk.identity.exceptions.ErrorCode;
import com.hhk.identity.mapper.ProfileMapper;
import com.hhk.identity.mapper.UserMapper;
import com.hhk.identity.repositories.RoleRepository;
import com.hhk.identity.repositories.UserRepository;
import com.hhk.identity.repositories.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
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

            profileClient.createProfile(profileRequest);
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

    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
