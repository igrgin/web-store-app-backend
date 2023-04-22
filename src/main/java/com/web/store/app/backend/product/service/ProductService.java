package com.web.store.app.backend.product.service;

import com.web.store.app.backend.product.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<List<ProductDTO>> searchProducts(String name, String category);

    Optional<ProductDTO> saveProduct(ProductDTO productDTO);

    Optional<List<ProductDTO>> findAll();

    Optional<ProductDTO> findById(String id);

    Optional<List<ProductDTO>> findByCategory(String category);

    Optional<List<ProductDTO>> findByBrand(String brand);

    void deleteProductsById(List<String> ids);


    Optional<ProductDTO> updateById(ProductDTO productDTO);

}
