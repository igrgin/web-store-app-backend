package com.web.store.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record CategoryDTO(@NotNull @Positive Integer id, @NotEmpty String name, Integer parentId) {


}
