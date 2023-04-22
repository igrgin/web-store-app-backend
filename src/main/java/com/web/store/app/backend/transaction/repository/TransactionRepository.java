package com.web.store.app.backend.transaction.repository;

import com.web.store.app.backend.transaction.document.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {

    List<Transaction> findTransactionsByUserId(Long userId);

}
