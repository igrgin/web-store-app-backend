package com.web.store.app.backend.payment.process.cart.service;

import com.web.store.app.backend.payment.process.cart.document.CartProduct;
import com.web.store.app.backend.payment.process.cart.dto.CartProductDto;
import com.web.store.app.backend.payment.process.cart.repository.CartProductRepository;
import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.service.ProductService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartProductServiceImpl implements CartProductService {

    private final CartProductRepository repository;
    private  final ProductService productService;

    @Override
    public String saveCartProductBulk(List<CartProductDto> cartProducts) {
        var cartId = generateUniqueCartId();
        repository.saveAll(cartProducts.stream().map(cartProductDto -> {
            cartProductDto.setId(cartId);
            System.out.println(cartProductDto);
            return mapToCartProduct(cartProductDto);
        }).toList());

        return cartId;
    }

    @Override
    public void deleteCartProductsByCartId(String cartId) {
            var products = repository.getCartProductsByCartId(cartId);
            repository.deleteAll(products);
    }

    @Override
    public List<CartProductDto> getCartsByBrand(String brand) {

        var productsByBrandPage = productService.findByBrand(brand, 0, 1000);
        var totalPages = productsByBrandPage.getTotalPages();
        if(productsByBrandPage.getProducts().isEmpty()) return List.of();
        var soldProductsByBrand = new ArrayList<>(repository.getCartProductByProductIdIsIn(productsByBrandPage.getProducts().stream()
                .map(ProductDTO::id).toList()).stream().map(this::mapToCartProductDto).toList());
        for (int i = 0; i < totalPages+1; i++) {
            productsByBrandPage = productService.findByBrand(brand, i, 1000);
            var productIdsQuantityPage = repository.getCartProductByProductIdIsIn(productsByBrandPage.getProducts().stream()
                    .map(ProductDTO::id).toList()).stream().map(this::mapToCartProductDto).toList();

            soldProductsByBrand.addAll(productIdsQuantityPage);
        }

        return soldProductsByBrand;

    }

    @Override
    public List<CartProductDto> getCartsByCartId(String cartId) {

        System.out.println(cartId);
        return repository.getCartProductsByCartId(cartId).stream().map(this::mapToCartProductDto).toList();

    }

    @Override
    public List<ProductDTO> getProductByCartId(String cartId) {
        var productIds = repository.getCartProductsByCartId(cartId).stream().map(CartProduct::getProductId).toList();
        System.out.println(productIds);
        var products = new LinkedList<ProductDTO>();
        productIds.forEach(productId -> productService.findById(productId).map(products::add));
        System.out.println(products);
        return products;
    }

    private CartProduct mapToCartProduct(CartProductDto cartProductDto) {
        return new CartProduct(null,cartProductDto.getId(),
                cartProductDto.getProductId(), cartProductDto.getQuantity());
    }
    private CartProductDto mapToCartProductDto(CartProduct cartProduct) {
        return new CartProductDto(cartProduct.getCartId(), cartProduct.getQuantity(),
                cartProduct.getProductId());
    }
    private String generateUniqueCartId(){
        var cartId = UUID.randomUUID().toString();
        while(repository.existsByCartId(cartId)) cartId = UUID.randomUUID().toString();
        return cartId;
    }
}
