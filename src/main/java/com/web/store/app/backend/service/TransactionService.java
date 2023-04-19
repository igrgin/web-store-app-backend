package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.TransactionDTO;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Optional<List<TransactionDTO>> findUserTransactions(Integer userId);
}
