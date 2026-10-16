package com.ticketrush.commonweb.error;

import com.ticketrush.commonweb.filter.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class GlobalExceptionHandlerSupport {

    protected ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String detail,
            HttpServletRequest request,
            List<ApiError.FieldErrorDetail> errors) {
        ApiError body = new ApiError(
                "about:blank",
                status.getReasonPhrase(),
                status.value(),
                detail,
                request.getRequestURI(),
                MDC.get(CorrelationIdFilter.MDC_KEY),
                Instant.now(),
                errors

        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<ApiError.FieldErrorDetail> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiError.FieldErrorDetail(error.getField(),
                        error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"))
                .toList();

        return buildError(HttpStatus.BAD_REQUEST, "Request Validation failed", request, errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException exception, HttpServletRequest request) {
        List<ApiError.FieldErrorDetail> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiError.FieldErrorDetail(error.getField(),
                        error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"))
                .toList();

        return buildError(HttpStatus.BAD_REQUEST, "Request Validation Failed", request, errors);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException exception,
            HttpServletRequest request) {
        List<ApiError.FieldErrorDetail> errors = exception.getConstraintViolations().stream()
                .map(violation -> new ApiError.FieldErrorDetail(violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList();

        return buildError(HttpStatus.BAD_REQUEST, "Constraint Validation Failed", request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Malformed or missing request body",
                request,
                List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method not supported for this endpoint",
                request,
                List.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content type not supported",
                request,
                List.of());
    }

}
