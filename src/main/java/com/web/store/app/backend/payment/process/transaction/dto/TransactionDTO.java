package com.web.store.app.backend.payment.process.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.web.store.app.backend.payment.process.cart.dto.CartProductDto;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
@Builder
public class TransactionDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    private ZonedDateTime createdAt;
    @JsonProperty("cart_id")
    private String cartId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("price")
    private String price;
    @JsonProperty("cart_product")
    private List<CartProductDto> cartProduct;

}

