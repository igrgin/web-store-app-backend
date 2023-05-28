package com.web.store.app.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PageableProductsDTO {
    @NonNull
    private final List<ProductDTO> products;
    @JsonProperty("total_pages")
    private Long totalPages;
    @JsonProperty("total_products")
    private final Long totalProducts;

}
