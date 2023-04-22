package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.transaction.dto.TransactionDTO;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Optional<List<TransactionDTO>> findUserTransactions(Long userId);

    Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO);
}
