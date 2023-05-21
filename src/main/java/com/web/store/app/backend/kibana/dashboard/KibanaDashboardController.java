package com.web.store.app.backend.kibana.dashboard;

import com.web.store.app.backend.kibana.dashboard.service.KibanaDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kibana-dashboard/api")
@AllArgsConstructor
public class KibanaDashboardController {

    private KibanaDashboardService kibanaDashboardService;
    private Map<String, String> errors;

    @GetMapping("/dashboard/{name}")
    public ResponseEntity<String> getDashboard(@PathVariable("name") String name) {

        System.out.println("HERE");
        return kibanaDashboardService.getDashboard(name).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .build());
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
