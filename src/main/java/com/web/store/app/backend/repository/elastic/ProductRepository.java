package com.web.store.app.backend.repository.elastic;

import com.web.store.app.backend.entity.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    @Override
    @Nonnull
    List<Product> findAll();

    List<Product> findAllByCategory(String category);

    List<Product> findAllByBrand(String brand);

    @Query("{\"bool\": {\"must\": [{\"regexp\": {\"name\": \".*?0.*\"}}, {\"match\": {\"category\": \"?1\"}}]}}")
    List<Product> findProductByNameIgnoreCaseAndCategory(String name, String category);

    void deleteByIdIn(List<String> ids);

    Boolean existsProductById(String id);


}

