
package com.example.fixmate.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenExceptin extends RuntimeException {
    public ForbiddenExceptin(String message) {
        super(message);
    }
}
