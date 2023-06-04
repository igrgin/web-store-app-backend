package com.web.store.app.backend.payment.process.cart.service;

import com.web.store.app.backend.payment.process.cart.dto.CartProductDto;
import com.web.store.app.backend.product.dto.ProductDTO;
import java.util.List;

public interface CartProductService {

    String saveCartProductBulk(List<CartProductDto> cartProducts);

    void deleteCartProductsByCartId(String cartId);

    List<CartProductDto> getCartsByBrand(String brand);

    List<CartProductDto> getCartsByCartId(String cartId);

    List<ProductDTO> getProductByCartId(String cartId);
}
