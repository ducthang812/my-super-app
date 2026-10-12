package com.example.my_super_app.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceConflictException  extends ApiException{
    public ResourceConflictException(String code, String message) {
        super(code, message, HttpStatus.CONFLICT);
    }
}
