package com.web.store.app.backend.transaction.repository;

import com.web.store.app.backend.transaction.document.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {

    Page<Transaction> findTransactionsByUserId(Long userId, Pageable pageable);

}
