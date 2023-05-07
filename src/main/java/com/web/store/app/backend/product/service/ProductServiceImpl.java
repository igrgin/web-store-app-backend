package com.web.store.app.backend.product.service;

import com.web.store.app.backend.product.document.Product;
import com.web.store.app.backend.product.dto.PageableProductsDTO;
import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public Optional<PageableProductsDTO> searchProducts(String name, String category, List<String> brands, Integer page, Integer size, Integer priceMin, Integer priceMax) {

        return brands.isEmpty() ? Optional.of(productRepository.productSearchWithoutBrand(name,category,priceMin,priceMax, PageRequest.of(page,size)))
                .map(ProductServiceImpl::mapToProductWrapperDTO):
                Optional.of(productRepository.productSearchWithBrand(name,category,brands,priceMin,priceMax,
                                PageRequest.of(page,size))).map(ProductServiceImpl::mapToProductWrapperDTO);
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
                productDTO.price(), productDTO.category(),productDTO.imageURL(),
                productDTO.stock(), productDTO.description());
    }

    private static ProductDTO mapToProductDto(Product product) {
        return new ProductDTO(product.getId(), product.getBrand(), product.getName(),
                product.getPrice(), product.getCategory(), product.getStock(), product.getDescription(),
                product.getImageURL());
    }

    private static PageableProductsDTO mapToProductWrapperDTO(Page<Product> products) {

        var productsToSend = products.stream().map(ProductServiceImpl::mapToProductDto).toList();
        return new PageableProductsDTO(productsToSend, products.getTotalPages());
    }
}