package com.example.fixmate.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAutherizedException extends RuntimeException {

    public UnAutherizedException(String message) {
        super(message);
    }
}
