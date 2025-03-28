package shared.hub.controller.http;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import shared.hub.application.model.ApiResponse;
import shared.hub.application.model.request.CreateUserRequest;
import shared.hub.application.model.request.UpdateUserRolesRequest;
import shared.hub.application.model.response.UserResponse;
import shared.hub.application.service.user.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<Page<UserResponse>> getUsers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "false") boolean asc) {
        Sort.Direction soDirection = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(soDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        return ApiResponse.<Page<UserResponse>>builder()
                .data(userService.getUsers(pageable))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUser(userId))
                .build();
    }

    @GetMapping("/info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder().data(userService.getMyInfo()).build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().data("User has been deleted").build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUserRoles(
            @PathVariable String userId, @RequestBody UpdateUserRolesRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUserRoles(userId, request))
                .build();
    }
}
