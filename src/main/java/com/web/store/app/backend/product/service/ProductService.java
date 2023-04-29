package com.web.store.app.backend.product.service;

import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.dto.PageableProductsDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<PageableProductsDTO> searchProducts(String name, String category, String brand, Integer page, Integer size);

    Optional<ProductDTO> saveProduct(ProductDTO productDTO);

    Optional<PageableProductsDTO> findAll(Integer page, Integer size);

    Optional<ProductDTO> findById(String id);

    Optional<PageableProductsDTO> findByCategory(String category, Integer page, Integer size);

    Optional<PageableProductsDTO> findByBrand(String brand, Integer page, Integer size);

    void deleteProductsById(List<String> ids);


    Optional<ProductDTO> updateById(ProductDTO productDTO);

}