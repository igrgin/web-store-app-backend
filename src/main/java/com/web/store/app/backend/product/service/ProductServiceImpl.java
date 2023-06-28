package com.web.store.app.backend.product.service;

import com.web.store.app.backend.product.document.Product;
import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.repository.ProductRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ElasticsearchOperations operations;

    public PageableProductsDTO searchProducts(String name, String category, String subcategory, String brands,
                                              Integer page, Integer size, Integer priceMin, Integer priceMax) {

        var queryBuilder = buildQueryString(name, category, subcategory, brands, priceMin, priceMax);
        var query = new StringQuery(queryBuilder.toString(), PageRequest.of(page, size));
        SearchHits<Product> searchHits = operations.search(query, Product.class, IndexCoordinates.of("product"));
        var productDtos = searchHits.stream().map(SearchHit::getContent)
                .map(ProductServiceImpl::mapToProductDto).toList();
        return new PageableProductsDTO(productDtos, null, searchHits.getTotalHits());
    }

    private static StringBuilder buildQueryString(String name, String category, String subcategory, String brands, Integer priceMin, Integer priceMax) {
        var isFirst = false;
        var queryBuilder = new StringBuilder();

        if (name != null)
            queryBuilder.append("{\"bool\": { \"should\": [ {\"match\": { \"description\": \"" + name + "\"}}],  \"filter\": [");
        else queryBuilder.append("""
                {"bool": {
                      "filter": [""");
        if (category != null) {
            isFirst = true;
            queryBuilder.append("{\n" +
                    "          \"term\": {\n" +
                    "            \"category\": \"" + category + "\"\n" +
                    "          }\n" +
                    "        }");
        }

        if (subcategory != null) {
            if (!isFirst) {
                isFirst = true;
            } else {
                queryBuilder.append(",");
            }
            queryBuilder.append("{\n" +
                    "          \"term\": {\n" +
                    "            \"subcategory\": \"" + subcategory + "\"\n" +
                    "          }\n" +
                    "        }");
        }
        if (priceMax != null && priceMin != null) {
            if (!isFirst) {
                isFirst = true;
            } else {
                queryBuilder.append(",");
            }
            queryBuilder.append("{\n" +
                    "          \"range\": {\n" +
                    "            \"price\": {\n" +
                    "              \"gte\":" + priceMin + ",\n" +
                    "              \"lte\": " + priceMax + "\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }");
        }
        if (name != null) {
            if (!isFirst) {
                isFirst = true;
            } else {
                queryBuilder.append(",");
            }
            queryBuilder.append("{\n" +
                    "          \"wildcard\": {\n" +
                    "            \"name\": {\n" +
                    "              \"value\":\"*" + name + "*\",\n" +
                    "              \"case_insensitive\":true\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }");
        }
        if (brands != null) {
            if (isFirst) {
                queryBuilder.append(",");
            }
            var brandsFilter = Arrays.stream(brands.split(","))
                    .map(item -> "\"" + item + "\"")
                    .collect(Collectors.joining(","));
            queryBuilder.append("{\n" +
                    "          \"terms\": {\n" +
                    "            \"brand\": [" + brandsFilter + "]}\n" +
                    "        }");
        }
        queryBuilder.append("""
                ]
                    }
                  }
                """);
        return queryBuilder;
    }

    public void changeStock(String id, int quantity)
    {
        var updateQuery = UpdateQuery.builder(id)
                .withScriptType(ScriptType.INLINE)
                .withScript("ctx._source.stock -= params.quantity")
                .withParams(Collections.singletonMap("quantity", quantity))
                .build();

        // Execute the update operation
        operations.update(updateQuery, IndexCoordinates.of("product"));
    }

    public Optional<ProductDTO> saveProduct(ProductDTO productDTO) {

        var doesExist = productRepository.existsProductById(productDTO.id());

        if (doesExist) {
            log.info("Product with id:{} already exist", productDTO.id());
            return Optional.empty();
        }

        return Optional.of(mapToProductDto(productRepository.save(mapToProduct(productDTO))));
    }

    public Optional<PageableProductsDTO> findAll(Integer page, Integer size) {

        return Optional.of(productRepository.findAll(PageRequest.of(page, size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO);

    }

    public Optional<ProductDTO> findById(String id) {
        return productRepository.findById(id).map(ProductServiceImpl::mapToProductDto);
    }

    public Optional<PageableProductsDTO> findByCategory(String category, Integer size) {
        return Optional.of(productRepository
                        .findAllByCategory(category, PageRequest.ofSize(size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO);
    }

    public Optional<PageableProductsDTO> findBySubcategory(String subcategory, Integer page, Integer size) {
        return Optional.of(productRepository
                        .findAllBySubcategory(subcategory, PageRequest.of(page, size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO);
    }

    public PageableProductsDTO findByBrand(String brand, Integer page, Integer size) {
        return mapToProductWrapperDTO(productRepository.findAllByBrand(brand, PageRequest.of(page, size)));
    }

    public void deleteProductById(String id) {
        productRepository.deleteById(id);
    }

    public Optional<ProductDTO> updateById(ProductDTO productDTO) {

        var doesExist = productRepository.existsProductById(productDTO.id());

        if (!doesExist) {
            log.info("Product with id:{} doesn't exist", productDTO.id());
            return Optional.empty();
        }

        return Optional.of(mapToProductDto(productRepository.save(mapToProduct(productDTO))));
    }


    private static Product mapToProduct(ProductDTO productDTO) {
        return new Product(productDTO.id(), productDTO.brand(), productDTO.name(),
                productDTO.price(), productDTO.category(), productDTO.category(), productDTO.imageURL(),
                productDTO.stock(), productDTO.description());
    }

    private static ProductDTO mapToProductDto(Product product) {
        return new ProductDTO(product.getId(), product.getBrand(), product.getName(),
                product.getPrice(), product.getCategory(), product.getSubcategory(), product.getStock(),
                product.getDescription(), product.getImageURL());
    }

    private static PageableProductsDTO mapToProductWrapperDTO(Page<Product> products) {

        var productsToSend = products.stream().map(ProductServiceImpl::mapToProductDto).toList();
        return new PageableProductsDTO(productsToSend, (long) (products.getTotalPages() + 1), products.getTotalElements());
    }
}
