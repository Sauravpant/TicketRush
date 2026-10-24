package com.ticketrush.catalogservice.exception;

import com.ticketrush.commonweb.error.ApiError;
import com.ticketrush.commonweb.error.GlobalExceptionHandlerSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class CatalogExceptionHandler extends GlobalExceptionHandlerSupport {

    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<ApiError> handleVenueNotFound(VenueNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(VenueSectionNotFoundException.class)
    public ResponseEntity<ApiError> handleVenueSectionNotFound(VenueSectionNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(VenueSeatNotFoundException.class)
    public ResponseEntity<ApiError> handleVenueSeatNotFound(VenueSeatNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ApiError> handleEventNotFound(EventNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(PriceTierNotFoundException.class)
    public ResponseEntity<ApiError> handlePriceTierNotFound(PriceTierNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(OrganizerAccessDeniedException.class)
    public ResponseEntity<ApiError> handleOrganizerAccessDenied(OrganizerAccessDeniedException ex, HttpServletRequest req) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLockConflict(ObjectOptimisticLockingFailureException ex, HttpServletRequest req) {
        return buildError(
                HttpStatus.CONFLICT,
                "This resource was modified by someone else — refresh and try again",
                req,
                List.of());
    }
}
