package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.ProductDTO;
import com.web.store.app.backend.entity.Product;
import com.web.store.app.backend.repository.elastic.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public Optional<List<ProductDTO>> searchProducts(String name, String category) {

        return Optional.of(productRepository.findProductByNameIgnoreCaseAndCategory(name, category)
                .stream().map(ProductServiceImpl::mapToProductDto).toList());
    }

    public Optional<ProductDTO> saveProduct(ProductDTO productDTO) {

        var doesExist = productRepository.existsProductById(productDTO.id());

        if (doesExist) {
            log.info("Product with id:{} already exist", productDTO.id());
            return Optional.empty();
        }

        return Optional.of(mapToProductDto(productRepository.save(mapToProduct(productDTO))));
    }

    public Optional<List<ProductDTO>> findAll() {

        return Optional.of(productRepository
                .findAll().stream()
                .map(ProductServiceImpl::mapToProductDto)
                .toList());
    }

    public Optional<ProductDTO> findById(String id) {
        return productRepository.findById(id).map(ProductServiceImpl::mapToProductDto);
    }

    public Optional<List<ProductDTO>> findByCategory(String category) {
        return Optional.of(productRepository
                .findAllByCategory(category).stream()
                .map(ProductServiceImpl::mapToProductDto)
                .toList());
    }

    public Optional<List<ProductDTO>> findByBrand(String brand) {
        return Optional.of(productRepository
                .findAllByBrand(brand).stream()
                .map(ProductServiceImpl::mapToProductDto)
                .toList());
    }

    public void deleteProductsById(List<String> ids) {
        productRepository.deleteByIdIn(ids);
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
        return new Product(productDTO.id(), productDTO.brand(), productDTO.model(), productDTO.name(),
                productDTO.price(), productDTO.category(), productDTO.stock(), productDTO.description());
    }

    private static ProductDTO mapToProductDto(Product product) {
        return new ProductDTO(product.getId(), product.getBrand(), product.getModel(), product.getName(),
                product.getPrice(), product.getCategory(), product.getStock(), product.getDescription());
    }
}