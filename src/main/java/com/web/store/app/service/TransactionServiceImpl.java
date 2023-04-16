package com.web.store.app.service;

import com.web.store.app.dto.TransactionDTO;
import com.web.store.app.entity.Transaction;
import com.web.store.app.repository.elastic.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    public Optional<List<TransactionDTO>> findUserTransactions(Integer userId) {
        return Optional.of(transactionRepository.findTransactionByUserId(userId).stream()
                .map(TransactionServiceImpl::mapToTransactionDTO).toList());
    }

    private static Transaction mapToTransaction(TransactionDTO transactionDTO) {
        return new Transaction(transactionDTO.id(), transactionDTO.userId(), transactionDTO.createdAt(),
                transactionDTO.productIds());
    }

    private static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getUserId(), transaction.getCreatedAt(),
                transaction.getProductIds());

    }

}
