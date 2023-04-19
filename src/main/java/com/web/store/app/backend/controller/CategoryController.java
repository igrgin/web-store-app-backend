package com.web.store.app.backend.controller;

import com.web.store.app.backend.dto.CategoryDTO;
import com.web.store.app.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category/api/")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;
    private Map<String, String> errors;

    @GetMapping("find/all")
    private ResponseEntity<List<CategoryDTO>> getAllCategories() {

        return categoryService.findAll().map(categoryDTOS -> ResponseEntity.status(HttpStatus.OK).body(categoryDTOS))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());

    }

    @GetMapping("find/{id}")
    private ResponseEntity<CategoryDTO> getCategoriesById(@PathVariable Integer id) {

        return categoryService.findById(id)
                .map(categoryDTO -> ResponseEntity.status(HttpStatus.OK).body(categoryDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());

    }

    @PostMapping("add")
    private ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

        return categoryService.save(categoryDTO)
                .map(categoryDTO1 -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(categoryDTO1)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCategoriesById(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
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
