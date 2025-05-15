package shared.hub.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shared.hub.auth.dto.response.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(ApiResponse.fail(exception.getErrorCode().getMessage(), exception.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.info("Error handle {}",exception.getMessage());
        var err = NotifyCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(err.getStatusCode())
                .body(ApiResponse.fail("", err));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse> handlingCommonException(RuntimeException exception) {
        var err = NotifyCode.COMMON_ERROR;
        return ResponseEntity
                .status(err.getStatusCode())
                .body(ApiResponse.fail("", err));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        var err = NotifyCode.INVALID_PARAM;
        return ResponseEntity
                .status(err.getStatusCode())
                .body(ApiResponse.<Map<String,String>>builder()
                        .code(err.getCode())
                        .data(errors)
                        .message(err.getMessage())
                        .build());
    }
}
