package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.transaction.dto.TransactionDTO;

import java.util.Optional;

public interface TransactionService {

    Optional<PageableTransactionsDTO> findUserTransactions(String authorizationHeader, Integer page,Integer size);

    Optional<TransactionDTO> saveTransaction(String authorizationHeader, TransactionDTO transactionDTO);
}
