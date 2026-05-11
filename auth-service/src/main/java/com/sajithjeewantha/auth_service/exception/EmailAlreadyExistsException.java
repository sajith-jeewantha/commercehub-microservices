package com.sajithjeewantha.auth_service.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
