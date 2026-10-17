package com.ticketrush.authservice.exception;

import com.ticketrush.commonweb.error.ApiError;
import com.ticketrush.commonweb.error.GlobalExceptionHandlerSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandlerSupport {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest req) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest req) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiError> handleInvalidRefreshToken(InvalidRefreshTokenException ex, HttpServletRequest req) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiError> handleAccountLocked(AccountLockedException ex, HttpServletRequest req) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), req, List.of());
    }
}
