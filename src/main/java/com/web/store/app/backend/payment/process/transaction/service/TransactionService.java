package com.web.store.app.backend.payment.process.transaction.service;

import com.web.store.app.backend.payment.process.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.payment.process.transaction.dto.TransactionDTO;
import java.util.Optional;

public interface TransactionService {

    Optional<PageableTransactionsDTO> findUserTransactions(String authorizationHeader, Integer page, Integer size);

    PageableTransactionsDTO findTransactionsByBrand(String brand, Integer page, Integer size);

    Optional<TransactionDTO> saveTransaction(String authorizationHeader, TransactionDTO transactionDTO);
}
