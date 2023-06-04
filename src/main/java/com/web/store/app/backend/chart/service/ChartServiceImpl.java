package com.web.store.app.backend.chart.service;

import com.web.store.app.backend.payment.process.cart.dto.CartProductDto;
import com.web.store.app.backend.payment.process.cart.service.CartProductService;
import com.web.store.app.backend.product.service.ProductService;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final CartProductService cartProductService;
    private final ProductService productService;
    public Map<String, Integer> getTopProductsByBrand(String brand, Integer columnCount) {

        var cartProducts = cartProductService.getCartsByBrand(brand);


        return cartProducts.stream()
                .collect(Collectors.groupingBy(
                        CartProductDto::getProductId,
                        Collectors.summingInt(CartProductDto::getQuantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(columnCount)
                .collect(Collectors.toMap(
                        entry -> productService.findById(entry.getKey()).get().name(),
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

    }
}
