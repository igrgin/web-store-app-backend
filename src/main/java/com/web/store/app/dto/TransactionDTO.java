package com.web.store.app.dto;

import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.List;


public record TransactionDTO(String id, @Positive Integer userId, Date createdAt, List<Long> productIds) {
}
