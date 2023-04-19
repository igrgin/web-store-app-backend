package com.web.store.app.backend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDTO(String id, @Positive Integer userId, Date createdAt, List<Long> productIds) {
}
