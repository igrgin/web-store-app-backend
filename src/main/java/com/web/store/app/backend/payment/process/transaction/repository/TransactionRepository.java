package com.web.store.app.backend.payment.process.transaction.repository;

import com.web.store.app.backend.payment.process.transaction.document.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {

    Page<Transaction> findTransactionsByUserId(Long userId, Pageable pageable);
    Transaction findTransactionsByCartId(String cartId);

}
