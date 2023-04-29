package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.transaction.dto.TransactionDTO;

import java.util.Optional;

public interface TransactionService {

    Optional<PageableTransactionsDTO> findUserTransactions(Long userId, Integer page,Integer size);

    Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO);
}
