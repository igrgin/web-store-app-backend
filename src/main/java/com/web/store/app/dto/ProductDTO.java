package com.web.store.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDTO(@NotEmpty String id, @NotEmpty String brand, @NotEmpty String model, @NotEmpty String name,
                         @NotNull @PositiveOrZero Float price, @NotEmpty String category,
                         @NotNull @PositiveOrZero Long stock,
                         @NotEmpty String description) {
}
