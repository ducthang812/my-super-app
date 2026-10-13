package com.example.my_super_app.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenActionException extends ApiException {
    public ForbiddenActionException(String code, String message) {
        super(code, message, HttpStatus.FORBIDDEN);
    }
}
