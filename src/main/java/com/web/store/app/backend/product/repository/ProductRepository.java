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

    @Query("{\"bool\":{\"should\":[{\"wildcard\":{\"name\":\"*?0*\"}},{\"term\":{\"category\":\"?1\"}},{\"terms\":{\"brand\": ?2}},{\"range\":{\"price\":{\"gte\":?3,\"lte\":?4}}}]}}")
    Page<Product> findProductByNameIgnoreCaseAndCategoryAndBrandIn(String name, String category, List<String> brands,
                                                                   Integer priceMin, Integer priceMax, Pageable pageable);
    Boolean existsProductById(String id);


}

