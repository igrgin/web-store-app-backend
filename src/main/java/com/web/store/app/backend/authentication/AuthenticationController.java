package com.web.store.app.backend.authentication;

import com.web.store.app.backend.authentication.model.AuthenticationRequest;
import com.web.store.app.backend.authentication.model.AuthenticationResponse;
import com.web.store.app.backend.authentication.model.RegisterRequest;
import com.web.store.app.backend.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/api")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private Map<String, String> errors;

    @PostMapping("/register")
    private ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService.authenticate(request).map(authenticationResponse ->
                        ResponseEntity.status(HttpStatus.OK).body(authenticationResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/refresh")
    private ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
         authenticationService.refreshToken(request,response);
         return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(
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
    private Map<String, String> handleExceptions(
            Exception ex) {
        errors = new HashMap<>();
        String fieldName = ex.getClass().getSimpleName();
        String errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
