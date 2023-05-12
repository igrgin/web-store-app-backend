package com.web.store.app.backend.product.repository;

import com.web.store.app.backend.product.document.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ProductRepository extends ElasticsearchRepository<Product, String> {


    @Nonnull
    Page<Product> findAll(@Nonnull Pageable pageable);

    Page<Product> findAllByCategory(String category,@Nonnull Pageable pageable);

    Page<Product> findAllByBrand(String brand, @Nonnull Pageable pageable);
    Boolean existsProductById(String id);


}

