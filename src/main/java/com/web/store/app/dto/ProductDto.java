package com.web.store.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductDto( @NotEmpty String id, @NotEmpty String brand, @NotEmpty String model, @NotEmpty String name,
                          @NotNull Float price, @NotEmpty String category, @NotNull Long stock,
                          @NotEmpty String description) {
}
