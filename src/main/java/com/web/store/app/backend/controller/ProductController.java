package com.web.store.app.backend.controller;

import com.web.store.app.backend.dto.ProductDTO;
import com.web.store.app.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product/api/")
@AllArgsConstructor
@CrossOrigin(origins = "http")
@Slf4j
public class ProductController {

    private ProductService productService;

    @GetMapping("search")
    private ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(value = "n") String name,
                                                            @RequestParam(value = "c") String category) {

        return productService.searchProducts(name, category).map(productDTOS -> ResponseEntity.status(HttpStatus.OK).body(productDTOS))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @GetMapping("find/all")
    private ResponseEntity<List<ProductDTO>> getAllProducts() {

        return productService.findAll().map(products1 -> ResponseEntity.status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/{id}")
    private ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {

        return productService.findById(id).map(
                productDto1 -> ResponseEntity.status(HttpStatus.OK).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/category/{category}")
    private ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@PathVariable String category) {

        return productService.findByCategory(category).map(products1 -> ResponseEntity.status(HttpStatus.OK)
                        .body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/brand/{brand}")
    private ResponseEntity<List<ProductDTO>> getAllProductsByBrand(@PathVariable String brand) {

        return productService.findByBrand(brand).map(products1 -> ResponseEntity.status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @PostMapping("add")
    private ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDto) {

        return productService.saveProduct(productDto).map(
                productDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @PutMapping("update")
    private ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) {

        return productService.updateById(productDTO).map(
                        productDto1 -> ResponseEntity
                                .status(HttpStatus.OK)
                                .body(productDto1))
                .orElseGet(
                        () -> ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .build());
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteProductsById(@RequestBody List<String> ids) {
        productService.deleteProductsById(ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleExceptions(
            Exception ex) {
        var errors = new HashMap<String, String>();
        var fieldName = ex.getClass().getSimpleName();
        var errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
