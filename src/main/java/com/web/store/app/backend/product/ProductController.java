package com.web.store.app.backend.product;

import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.service.ProductService;
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
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ProductController {

    private ProductService productService;

    @GetMapping("search")
    private ResponseEntity<PageableProductsDTO> searchProducts(@RequestParam(defaultValue = "", required = false) String name,
                                                               @RequestParam(defaultValue = "", required = false) String category,
                                                               @RequestParam(defaultValue = "", required = false) String brand,
                                                               final Integer page,
                                                               @RequestParam(defaultValue = "10") final Integer size) {

        return productService.searchProducts(name, category, brand, page, size)
                .map(productDTOS -> ResponseEntity.status(HttpStatus.OK).body(productDTOS))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("find/all")
    private ResponseEntity<PageableProductsDTO> getAllProducts(final Integer page,
                                                               @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findAll(page, size).map(products1 -> ResponseEntity
                        .status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/{id}")
    private ResponseEntity<ProductDTO> getProductById(@PathVariable final String id) {

        return productService.findById(id).map(
                productDto1 -> ResponseEntity.status(HttpStatus.OK).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/category/{category}")
    private ResponseEntity<PageableProductsDTO> getAllProductsByCategory(@PathVariable final String category, final Integer page,
                                                                         @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findByCategory(category, page, size).map(products1 -> ResponseEntity.status(HttpStatus.OK)
                        .body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("find/brand/{brand}")
    private ResponseEntity<PageableProductsDTO> getAllProductsByBrand(@PathVariable final String brand, final Integer page,
                                                                      @RequestParam(defaultValue = "10") final Integer size) {

        return productService.findByBrand(brand, page, size).map(products1 -> ResponseEntity.status(HttpStatus.OK).body(products1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @PostMapping("add")
    private ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody final ProductDTO productDto) {

        return productService.saveProduct(productDto).map(
                productDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(productDto1)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @PutMapping("update")
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

    @DeleteMapping("delete")
    private ResponseEntity<Void> deleteProductsById(@RequestBody final List<String> ids) {
        productService.deleteProductsById(ids);
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