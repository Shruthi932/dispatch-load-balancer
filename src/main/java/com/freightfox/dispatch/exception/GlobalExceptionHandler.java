
package com.freightfox.dispatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles DTO validation errors.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, Object> response = new HashMap<>();

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request data.");

        response.put("message", message);
        response.put("status", "error");

        return ResponseEntity.badRequest().body(response);
    }

    // Handles invalid JSON and invalid enum values.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(
            HttpMessageNotReadableException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("message",
                "Invalid JSON or unsupported field value. Check your request data.");
        response.put("status", "error");

        return ResponseEntity.badRequest().body(response);
    }

    // Handles unexpected application errors.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralErrors(
            Exception exception) {
        System.err.println("ROOT DEBUG: GLOBAL EXCEPTION HANDLER CALLED");
        exception.printStackTrace();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "An unexpected error occurred.");
        response.put("status", "error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
