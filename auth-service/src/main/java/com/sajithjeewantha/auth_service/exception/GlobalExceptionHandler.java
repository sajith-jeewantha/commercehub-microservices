package com.sajithjeewantha.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the entire application.
 * <p>
 * Handles:
 * - Business exceptions
 * - Validation errors
 * - Authentication/authorization errors
 * - Generic server errors
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom business exceptions.
     * <p>
     * Example:
     * - User already exists
     * - Resource not found
     * - Invalid operation
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", ex.getStatus().value(),
                        "error", ex.getStatus().getReasonPhrase(),
                        "message", ex.getMessage()
                ));
    }

    /**
     * Fallback handler for unexpected server errors.
     * <p>
     * Important:
     * Avoid exposing sensitive internal details in production.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "message", ex.getMessage()
                ));
    }

    /**
     * Handles invalid login credentials.
     * <p>
     * Spring Security throws this exception when:
     * - Email is incorrect
     * - Password is incorrect
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        "message", "Invalid email or password"
                ));
    }

    /**
     * Handles validation errors from @Valid DTOs.
     * <p>
     * Example:
     * - Invalid email format
     * - Password too short
     * - Required field missing
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "messages", validationErrors
                ));
    }

    /**
     * Handles locked account exceptions.
     * <p>
     * Example:
     * - Too many failed login attempts
     * - Admin locked the account
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(LockedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        "message", ex.getMessage()
                ));
    }

    /**
     * Handles disabled account exceptions.
     * <p>
     * Example:
     * - Account suspended
     * - Account inactive
     * - Account banned
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        "message", ex.getMessage()
                ));
    }
}
