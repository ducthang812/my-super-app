package com.example.my_super_app.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException{
    public InvalidCredentialsException(String code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }
}
