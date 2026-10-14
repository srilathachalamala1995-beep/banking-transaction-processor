package com.example.bank.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({BadRequestException.class, InsufficientFundsException.class})
    public ResponseEntity<Map<String, String>> badRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> conflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Account ID already exists or data violates a database constraint"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst().map(e -> e.getField() + ": " + e.getDefaultMessage())
            .orElse("Invalid request");
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
