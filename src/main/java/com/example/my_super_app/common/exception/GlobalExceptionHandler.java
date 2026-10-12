package com.example.my_super_app.common.exception;

import com.example.my_super_app.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //----Business/domain exception
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException apiException) {
        log.warn("Business exception: code = {}, message = {}", apiException.getCode(), apiException.getMessage());
        return ResponseEntity
                .status(apiException.getStatus())
                .body(ApiResponse.error(apiException.getCode(), apiException.getMessage()));
    }

    //---Valiation from @Valid in request body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        List<ApiResponse.FieldError> fieldErrors = exception.getBindingResult().getFieldErrors()
                .stream().map(fe -> new ApiResponse.FieldError(fe.getField(), fe.getDefaultMessage())).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("VALIDATION_ERROR", "Request validation failed", fieldErrors));
    }

    //Validation lỗi từ @Validated trên path/query param
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiResponse.FieldError> details = ex.getConstraintViolations().stream()
                .map(cv -> new ApiResponse.FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("VALIDATION_ERROR", "Request validation failed", details));
    }

//    Body gửi lên sai định dạng JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("MALFORMED_REQUEST", "Request body is not readable"));
    }

    // ---------- 5. Sai kiểu param, ví dụ ?id=abc nhưng cần Long ----------

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getName() + "' has invalid value";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("INVALID_PARAMETER", message));
    }

    // ---------- 6. Spring Security: sai username/password ----------

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("INVALID_CREDENTIALS", "Username or password is incorrect"));
    }

    // ---------- 7. Spring Security: không đủ quyền ----------

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("ACCESS_DENIED", "You do not have permission to perform this action"));
    }

    // ---------- 8. Vi phạm constraint ở DB, ví dụ UNIQUE(post_id, user_id) ----------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("DATA_CONFLICT", "Request conflicts with existing data"));
    }

    // ---------- 9. Fallback cho mọi exception chưa được handle ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_ERROR", "An unexpected error occurred"));
    }

}
