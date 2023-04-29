package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.transaction.document.Transaction;
import com.web.store.app.backend.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.transaction.dto.TransactionDTO;
import com.web.store.app.backend.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    public Optional<PageableTransactionsDTO> findUserTransactions(Long userId, Integer page, Integer size) {
        return Optional.of(transactionRepository.findTransactionsByUserId(userId, PageRequest.of(page,size)))
                .map(TransactionServiceImpl::mapToPageableTransactionDTO);
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

    private static PageableTransactionsDTO mapToPageableTransactionDTO(Page<Transaction> transactions) {

        var transactionToSend = transactions.stream().map(TransactionServiceImpl::mapToTransactionDTO).toList();
        return new PageableTransactionsDTO(transactionToSend, transactions.getTotalPages());
    }

}
