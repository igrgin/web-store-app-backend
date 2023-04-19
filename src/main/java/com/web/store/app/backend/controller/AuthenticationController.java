package com.web.store.app.backend.controller;

import com.web.store.app.backend.dto.AuthenticationRequest;
import com.web.store.app.backend.dto.AuthenticationResponse;
import com.web.store.app.backend.dto.RegisterRequest;
import com.web.store.app.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private Map<String, String> errors;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.of(Optional.ofNullable(authenticationService.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.of(Optional.ofNullable(authenticationService.authenticate(request)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleExceptions(
            Exception ex) {
        errors = new HashMap<>();
        String fieldName = ex.getClass().getSimpleName();
        String errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
