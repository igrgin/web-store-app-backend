package com.web.store.app.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryDTO(@NotNull @Positive Integer id, @NotEmpty String name, Integer parentId) {


}
