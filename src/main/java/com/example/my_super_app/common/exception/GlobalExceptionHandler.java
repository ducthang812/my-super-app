package com.example.my_super_app.common.exception;

import com.example.my_super_app.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
