package com.web.store.app.backend.repository.elastic;

import com.web.store.app.backend.entity.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {

    List<Transaction> findTransactionByUserId(Integer userId);

}
