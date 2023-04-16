package com.web.store.app.service;

import com.web.store.app.dto.TransactionDTO;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Optional<List<TransactionDTO>> findUserTransactions(Integer userId);
}
