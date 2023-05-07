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

    @Query("{\"bool\":{\"filter\":[{\"term\":{\"category\":\"?1\"}},{\"terms\":{\"brand\": ?2}},{\"range\":{\"price\":{\"gte\":?3,\"lte\":?4}}}],\"must\":[{\"wildcard\":{\"name\":\"*?0*\"}}]}}")
    Page<Product> productSearchWithBrand(String name, String category, List<String> brands,
                                         Integer priceMin, Integer priceMax, Pageable pageable);
    @Query("{\"bool\":{\"filter\":[{\"term\":{\"category\":\"?1\"}},{\"range\":{\"price\":{\"gte\":?2,\"lte\":?3}}}],\"must\":[{\"wildcard\":{\"name\":\"*?0*\"}}]}}")
    Page<Product> productSearchWithoutBrand(String name, String category,
                                            Integer priceMin, Integer priceMax, Pageable pageable);
    Boolean existsProductById(String id);


}

