package com.web.store.app.backend.brand;

import com.web.store.app.backend.brand.dto.BrandDTO;
import com.web.store.app.backend.brand.service.BrandService;
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
@RequestMapping("/brand/api")
@AllArgsConstructor
public class BrandController {

    private BrandService brandService;

    @GetMapping("/public/find/{id}")
    private ResponseEntity<BrandDTO> getBrandsById(@PathVariable Integer id) {

        return brandService.findById(id)
                .map(categoryDTO -> ResponseEntity.status(HttpStatus.OK).body(categoryDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());

    }

    @GetMapping("/public/find/subcategory/id/{categoryId}")
    private ResponseEntity<List<BrandDTO>> getBrandByCategoryId(@PathVariable("categoryId") Integer categoryId) {

        return Optional.of(brandService.findAllByCategoryId(categoryId))
                .map(categoryDTOs -> ResponseEntity.status(HttpStatus.OK).body(categoryDTOs))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @GetMapping("/public/find/category/name/{categoryName}")
    private ResponseEntity<List<BrandDTO>> getBrandByCategoryName(@PathVariable("categoryName") String categoryName) {

        return Optional.of(brandService.findAllByParentCategoryName(categoryName))
                .map(categoryDTOs -> ResponseEntity.status(HttpStatus.OK).body(categoryDTOs))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @DeleteMapping("/private/delete/{id}")
    private ResponseEntity<Void> deleteBrandsById(@PathVariable Integer id) {
        brandService.deleteById(id);
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
