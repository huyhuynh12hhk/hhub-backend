package shared.hub.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import shared.hub.auth.dto.request.CreateUserRequest;
import shared.hub.auth.dto.response.ApiResponse;
import shared.hub.auth.dto.response.UserResponse;
import shared.hub.auth.service.AppUserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsers(pageable);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> get(@PathVariable String username) {

        return userService.getUser(username);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }
}
