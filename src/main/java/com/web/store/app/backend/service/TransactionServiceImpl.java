package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.TransactionDTO;
import com.web.store.app.backend.entity.Transaction;
import com.web.store.app.backend.repository.elastic.TransactionRepository;
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

    public Optional<List<TransactionDTO>> findUserTransactions(Long userId) {
        return Optional.of(transactionRepository.findTransactionsByUserId(userId).stream()
                .map(TransactionServiceImpl::mapToTransactionDTO).toList());
    }

    public Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO) {
        return Optional.of(mapToTransactionDTO(transactionRepository.save(mapToTransaction(transactionDTO))));
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
