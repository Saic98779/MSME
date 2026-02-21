package com.metaverse.msme.exception;

import com.metaverse.msme.common.ApplicationAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle IllegalArgumentException - typically validation errors
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, String>>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                .success(false)
                .message(ex.getMessage())
                .code(400)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle RuntimeException - check if it's a "not found" error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, String>>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        // Check if it's a "not found" error
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("not found")) {
            ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                    .success(false)
                    .message(ex.getMessage())
                    .code(404)
                    .data(errorDetails)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Other runtime exceptions are treated as bad requests
        ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                .success(false)
                .message(ex.getMessage())
                .code(400)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, Object>>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, Object> errorDetails = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        errorDetails.put("fieldErrors", fieldErrors);
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        ApplicationAPIResponse<Map<String, Object>> response = ApplicationAPIResponse.<Map<String, Object>>builder()
                .success(false)
                .message("Validation failed")
                .code(400)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle AccessDeniedException - forbidden access
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, String>>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Access denied");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                .success(false)
                .message("Access denied: " + ex.getMessage())
                .code(403)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handle BadCredentialsException - authentication errors
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, String>>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Invalid credentials");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                .success(false)
                .message("Authentication failed: " + ex.getMessage())
                .code(401)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationAPIResponse<Map<String, String>>> handleGlobalException(
            Exception ex, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        errorDetails.put("stackTrace", getStackTrace(ex));
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        ApplicationAPIResponse<Map<String, String>> response = ApplicationAPIResponse.<Map<String, String>>builder()
                .success(false)
                .message("An unexpected error occurred: " + ex.getMessage())
                .code(500)
                .data(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Convert exception stack trace to string
     */
    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}

