package com.example.my_super_app.auth.controller;

import com.example.my_super_app.auth.dto.request.LoginRequest;
import com.example.my_super_app.auth.dto.request.RegisterRequest;
import com.example.my_super_app.auth.dto.response.AccountResponse;
import com.example.my_super_app.auth.dto.response.LoginResponse;
import com.example.my_super_app.auth.service.AuthService;
import com.example.my_super_app.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse accountResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(accountResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(loginResponse));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<AccountResponse>> getCurrentUser() {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        AccountResponse accountResponse = authService.getCurrentUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(accountResponse));
    }
}
