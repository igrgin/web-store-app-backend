package com.web.store.app.backend.product;

import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product/api")
@AllArgsConstructor
@Slf4j
public class ProductController {

    private ProductService productService;

    @GetMapping("/search")
    private ResponseEntity<PageableProductsDTO> searchProducts(@RequestParam(required = false,name = "name") String name,
                                                               @RequestParam(required = false, name = "category") String category,
                                                               @RequestParam(required = false, name = "brands") String brands,
                                                               @RequestParam(defaultValue = "0", name="page") final Integer page,
                                                               @RequestParam(defaultValue = "10", name="size") final Integer size,
                                                               @RequestParam(defaultValue = "0", required = false, name = "pMin") final Integer priceMin,
                                                               @RequestParam(defaultValue = "80", required = false, name = "pMax") final Integer priceMax) {

        return productService.searchProducts(name, category, brands == null || brands.isEmpty()? List.of() : Arrays.stream(brands.split(",")).toList(), page, size, priceMin, priceMax)
                .map(productDTOS -> ResponseEntity.status(HttpStatus.OK).body(productDTOS))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/find/all")
    private ResponseEntity<PageableProductsDTO> getAllProducts(final Integer page,
                                                               @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findAll(page, size).map(products1 -> ResponseEntity
                        .status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/find/{id}")
    private ResponseEntity<ProductDTO> getProductById(@PathVariable final String id) {

        return productService.findById(id).map(
                productDto1 -> ResponseEntity.status(HttpStatus.OK).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/find/category/{category}")
    private ResponseEntity<PageableProductsDTO> getAllProductsByCategory(@PathVariable final String category, final Integer page,
                                                                         @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findByCategory(category, page, size).map(products1 -> ResponseEntity.status(HttpStatus.OK)
                        .body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/find/brand/{brand}")
    private ResponseEntity<PageableProductsDTO> getAllProductsByBrand(@PathVariable final String brand, final Integer page,
                                                                      @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findByBrand(brand, page, size).map(products1 -> ResponseEntity.status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @PostMapping("/add")
    private ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody final ProductDTO productDto) {

        return productService.saveProduct(productDto).map(
                productDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @PutMapping("/update")
    private ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody final ProductDTO productDTO) {

        return productService.updateById(productDTO).map(
                        productDto1 -> ResponseEntity
                                .status(HttpStatus.OK)
                                .body(productDto1))
                .orElseGet(
                        () -> ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .build());
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteProductById(@PathVariable final String id) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(
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
    private Map<String, String> handleExceptions(
            Exception ex) {
        var errors = new HashMap<String, String>();
        var fieldName = ex.getClass().getSimpleName();
        var errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
