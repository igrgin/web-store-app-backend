package com.web.store.app.repository.elastic;

import com.web.store.app.entity.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TransactionRepository extends ElasticsearchRepository<Transaction,String> {
}
