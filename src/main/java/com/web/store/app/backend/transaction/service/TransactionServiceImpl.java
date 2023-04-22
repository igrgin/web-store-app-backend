package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.transaction.document.Transaction;
import com.web.store.app.backend.transaction.dto.TransactionDTO;
import com.web.store.app.backend.transaction.repository.TransactionRepository;
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
