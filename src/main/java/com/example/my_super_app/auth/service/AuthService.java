package com.example.my_super_app.auth.service;

import com.example.my_super_app.auth.dto.request.LoginRequest;
import com.example.my_super_app.auth.dto.request.RegisterRequest;
import com.example.my_super_app.auth.dto.response.AccountResponse;
import com.example.my_super_app.auth.dto.response.LoginResponse;

public interface AuthService {
    AccountResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    AccountResponse getCurrentUser(Long userId);
}
