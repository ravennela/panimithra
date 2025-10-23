package com.example.fixmate.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.fixmate.dtos.custom.ApiErrorDto;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleResouceNotFound(ResourceNotFoundException ex, WebRequest request) {
        System.out.println("resouce not found");
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(ForbiddenExceptin.class)
    public ResponseEntity<ApiErrorDto> handleForbiddenError(ForbiddenExceptin ex, WebRequest request) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiErrorDto> handleInternalServerError(InternalServerError ex, WebRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(UnAutherizedException.class)
    public ResponseEntity<ApiErrorDto> handleUnautherizedException(UnAutherizedException ex, WebRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getDescription(false));
    }

    private ResponseEntity<ApiErrorDto> buildError(HttpStatus status, String message, String path) {
        System.out.println("build method");
        ApiErrorDto error = new ApiErrorDto(
                status.value(), // HTTP status code (e.g. 404)
                status.getReasonPhrase(), // "Not Found", "Forbidden", etc.
                message, // Exception message
                path, null // Request URI (from request.getDescription(false))
        );
        return new ResponseEntity<>(error, status);
    }
};
