package com.web.store.app.backend.product.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.json.JsonData;
import com.web.store.app.backend.product.document.Product;
import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public PageableProductsDTO searchProducts(String name, String category, String subcategory, String brands,
                                              Integer page, Integer size, Integer priceMin, Integer priceMax) {

        var nativeQueryBuilder = new NativeQueryBuilder();
        nativeQueryBuilder.withQuery(builder -> {
            builder.bool(boolBuilder -> {
                if (category != null || subcategory != null || brands != null || (priceMax != null && priceMin != null)) {
                    boolBuilder.filter(filterBuilder -> {
                        if (category != null)
                            filterBuilder.term(termBuilder -> termBuilder.field("category").value(category));
                        if (category == null && subcategory != null)
                            filterBuilder.term(termBuilder -> termBuilder.field("subcategory").value(subcategory));
                        if (brands != null)
                            filterBuilder.terms(termsBuilder -> termsBuilder.field("brand")
                                    .terms(brandsBuilder -> brandsBuilder.value(Arrays.stream(brands.split(","))
                                            .toList().stream().map(FieldValue::of).toList())));
                        if (priceMax != null && priceMin != null)
                            filterBuilder.range(rangeBuilder -> rangeBuilder.field("price")
                                    .gte(JsonData.of(priceMin)).lte(JsonData.of(priceMax)));

                        return filterBuilder;
                    });
                }
                if (name != null) {
                    boolBuilder.must(mustBuilder -> mustBuilder.wildcard(wildecardBuilder -> wildecardBuilder
                            .field("name").value("*" + name + "*")));
                }

                return boolBuilder;
            });
            return builder;
        });

        nativeQueryBuilder.withPageable(PageRequest.of(page, size));
        var searchQuery = new NativeQuery(nativeQueryBuilder);
        var productHits = Optional.of(elasticsearchTemplate.search(searchQuery, Product.class));
        if (productHits.isEmpty()) {
            return new PageableProductsDTO(List.of(), 0);
        }
        var productDtos = productHits.get().stream().map(SearchHit::getContent)
                .map(ProductServiceImpl::mapToProductDto).toList();
        return new PageableProductsDTO(productDtos, page);
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

    public Optional<PageableProductsDTO> findByCategory(String category, Integer page, Integer size) {
        return Optional.of(productRepository
                        .findAllByCategory(category, PageRequest.of(page, size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO);
    }

    public Optional<PageableProductsDTO> findByBrand(String brand, Integer page, Integer size) {
        return Optional.of(productRepository
                        .findAllByBrand(brand, PageRequest.of(page, size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO);
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
        return new PageableProductsDTO(productsToSend, products.getTotalPages());
    }
}