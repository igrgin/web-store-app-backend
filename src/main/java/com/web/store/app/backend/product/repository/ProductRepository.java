package com.web.store.app.backend.product.repository;

import com.web.store.app.backend.product.document.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ProductRepository extends ElasticsearchRepository<Product, String> {


    @Nonnull
    Page<Product> findAll(@Nonnull Pageable pageable);

    Page<Product> findAllByCategory(String category,@Nonnull Pageable pageable);

    Page<Product> findAllByBrand(String brand, @Nonnull Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"regexp\": {\"name\": \".*?0.*\"}}, {\"match\": {\"category\": \"?1\"}}]}}")
    Page<Product> findProductByNameIgnoreCaseAndCategory(String name, String category, Pageable pageable);

    void deleteByIdIn(List<String> ids);

    Boolean existsProductById(String id);


}

