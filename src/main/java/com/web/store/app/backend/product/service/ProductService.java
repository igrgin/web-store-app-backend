package com.web.store.app.backend.product.service;

import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.dto.ProductDTO;

import java.util.Optional;

public interface ProductService {
    PageableProductsDTO searchProducts(String name, String category, String subcategory, String brands, Integer page,
                                       Integer size, Integer priceMin, Integer priceMax);

    Optional<ProductDTO> saveProduct(ProductDTO productDTO);

    Optional<PageableProductsDTO> findAll(Integer page, Integer size);

    Optional<ProductDTO> findById(String id);

    Optional<PageableProductsDTO> findByCategory(String category, Integer page, Integer size);
    Optional<PageableProductsDTO> findBySubcategory(String subcategory, Integer page, Integer size);

    Optional<PageableProductsDTO> findByBrand(String brand, Integer page, Integer size);

    void deleteProductById(String id);

    Optional<ProductDTO> updateById(ProductDTO productDTO);

}
