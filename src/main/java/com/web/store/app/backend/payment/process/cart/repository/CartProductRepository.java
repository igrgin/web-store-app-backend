package com.web.store.app.backend.payment.process.cart.repository;

import com.web.store.app.backend.payment.process.cart.document.CartProduct;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CartProductRepository extends ElasticsearchRepository<CartProduct,String> {

    List<CartProduct> getCartProductsByCartId(String cartId);
    List<CartProduct> getCartProductByProductIdIsIn(List<String> productIds);
    boolean existsByCartId(String cartId);
}
