package com.web.store.app.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.ZonedDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDTO(@JsonProperty("id") String id, @JsonProperty("user_id") Long userId,
                             @JsonProperty("created_at")
                             @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX", shape = JsonFormat.Shape.STRING)
                             ZonedDateTime createdAt,@JsonProperty("product_ids") List<String> productIds) {}

