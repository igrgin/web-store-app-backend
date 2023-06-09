package com.web.store.app.backend.category;

import com.web.store.app.backend.category.dto.CategoryDTO;
import com.web.store.app.backend.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/category/api")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/private/find/all")
    private ResponseEntity<List<CategoryDTO>> getAllCategories() {

        return categoryService.findAll().map(categoryDTOS -> ResponseEntity.status(HttpStatus.OK).body(categoryDTOS))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/public/find/category/{id}")
    private ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {

        return categoryService.findById(id)
                .map(categoryDTO -> ResponseEntity.status(HttpStatus.OK).body(categoryDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/public/find/subcategory/name/{categoryName}")
    private ResponseEntity<List<CategoryDTO>> getCategoriesByName(@PathVariable String categoryName) {

        return Optional.ofNullable(categoryService.findAllByParentCategoryName(categoryName))
                .map(categories -> ResponseEntity.status(HttpStatus.OK).body(categories))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/public/find/subcategory/id/{parentId}")
    private ResponseEntity<List<CategoryDTO>> getAllCategoriesByParentId(@PathVariable Integer parentId) {

        return Optional.of(categoryService.findAllByParentId(parentId))
                .map(categories -> ResponseEntity.status(HttpStatus.OK).body(categories))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/public/find/top")
    private ResponseEntity<List<CategoryDTO>> getAllMainCategories() {

        return Optional.of(categoryService.findAllByParentIdIsNull())
                .map(categories -> ResponseEntity.status(HttpStatus.OK).body(categories))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @PostMapping("/private/add")
    private ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {

        return categoryService.save(categoryDTO)
                .map(categoryDTO1 -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(categoryDTO1)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @DeleteMapping("/private/delete/{id}")
    private ResponseEntity<Void> deleteCategoriesById(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = new HashMap<String,String>();
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
        var errors = new HashMap<String,String>();
        String fieldName = ex.getClass().getSimpleName();
        String errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
