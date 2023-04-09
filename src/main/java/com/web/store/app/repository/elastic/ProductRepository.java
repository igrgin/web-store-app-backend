package com.web.store.app.repository.elastic;

import com.web.store.app.entity.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ProductRepository extends ElasticsearchRepository<Product,String> {

    @Override
    @Nonnull
    List<Product> findAll();

    List<Product> findAllByCategory(String category);

    List<Product> findAllByBrand(String brand);
}
