package shared.hub.gateway.controller.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shared.hub.gateway.application.model.ApiResponse;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    public ApiResponse<String> fallback() {
        return ApiResponse.<String>builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Service is temporarily unavailable. Please try again later.")
                .build();
    }
}
