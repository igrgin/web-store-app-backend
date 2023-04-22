package com.web.store.app.backend.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductDTO(@NotEmpty String id, @NotEmpty String brand, @NotEmpty String model, @NotEmpty String name,
                         @NotNull @PositiveOrZero Float price, @NotEmpty String category,
                         @NotNull @PositiveOrZero Long stock,
                         @NotEmpty String description) {
}
