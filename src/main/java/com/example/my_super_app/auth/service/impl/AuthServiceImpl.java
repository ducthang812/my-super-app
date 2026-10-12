package com.example.my_super_app.auth.service.impl;

import com.example.my_super_app.auth.dto.request.LoginRequest;
import com.example.my_super_app.auth.dto.request.RegisterRequest;
import com.example.my_super_app.auth.dto.response.AccountResponse;
import com.example.my_super_app.auth.dto.response.LoginResponse;
import com.example.my_super_app.auth.entity.Account;
import com.example.my_super_app.auth.mapper.AccountMapper;
import com.example.my_super_app.auth.repository.AccountRepository;
import com.example.my_super_app.auth.service.AuthService;
import com.example.my_super_app.common.Constant;
import com.example.my_super_app.common.exception.InvalidCredentialsException;
import com.example.my_super_app.common.exception.ResourceConflictException;
import com.example.my_super_app.common.security.JwtTokenProvider;
import com.example.my_super_app.user.service.UserProfileQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.expiration-ms}")
    public static final long TOKEN_EXPIRES_IN_SECONDS = 3600000;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountMapper accountMapper;
    private final UserProfileQueryService userProfileQueryService;

    public AuthServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AccountMapper accountMapper, UserProfileQueryService userProfileQueryService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountMapper = accountMapper;
        this.userProfileQueryService = userProfileQueryService;
    }

    @Override
    public AccountResponse register(RegisterRequest registerRequest) {
        if (accountRepository.existsByUsername(registerRequest.username())) {
            throw new ResourceConflictException(Constant.CodeException.USERNAME_ALREADY_EXISTS, Constant.MessageException.USERNAME_ALREADY_EXISTS);
        }
        if (accountRepository.existsByEmail(registerRequest.email())) {
            throw new ResourceConflictException(Constant.CodeException.EMAIL_ALREADY_EXITS, Constant.MessageException.EMAIL_ALREADY_EXITS);
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        Account account = new Account(registerRequest.username(), registerRequest.email(), hashedPassword);
        Account savedAccount = accountRepository.save(account);

        userProfileQueryService.createEmptyProfile(savedAccount.getId());

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Account account = accountRepository.findByUsernameOrEmail(loginRequest.usernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException(Constant.CodeException.INVALID_USERNAME_OR_PASSWORD, Constant.MessageException.INVALID_USERNAME_OR_PASSWORD));
        if (!passwordEncoder.matches(loginRequest.password(), account.getPasswordHash())) {
            throw new InvalidCredentialsException(Constant.CodeException.INVALID_USERNAME_OR_PASSWORD, Constant.MessageException.INVALID_USERNAME_OR_PASSWORD);
        }
        String token = jwtTokenProvider.generateToken(account.getId(), account.getUsername());
        return new LoginResponse(token, "Bearer", TOKEN_EXPIRES_IN_SECONDS);
    }

    @Override
    public AccountResponse getCurrentUser(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new InvalidCredentialsException(Constant.CodeException.INVALID_USERNAME_OR_PASSWORD, Constant.MessageException.INVALID_USERNAME_OR_PASSWORD));
        return accountMapper.toResponse(account);
    }
}
