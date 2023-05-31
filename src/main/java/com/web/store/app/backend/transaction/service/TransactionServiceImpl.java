package com.web.store.app.backend.transaction.service;

import com.web.store.app.backend.product.dto.ProductDTO;
import com.web.store.app.backend.product.service.ProductService;
import com.web.store.app.backend.security.service.JwtService;
import com.web.store.app.backend.transaction.document.Transaction;
import com.web.store.app.backend.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.transaction.dto.TransactionDTO;
import com.web.store.app.backend.transaction.model.TransactionStatus;
import com.web.store.app.backend.transaction.repository.TransactionRepository;
import com.web.store.app.backend.user.entity.AppUser;
import com.web.store.app.backend.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;
    private final AppUserService userService;
    private final ProductService productService;

    public Optional<PageableTransactionsDTO> findUserTransactions(String authorizationHeader, Integer page, Integer size) {

        final var userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        var userid = userService.findByEmail(userEmail).map(AppUser::getId);

        return Optional.of(transactionRepository.findTransactionsByUserId(userid.get(), PageRequest.of(page,size)))
                .map(TransactionServiceImpl::mapToPageableTransactionDTO);
    }

    public Optional<TransactionDTO> saveTransaction(String authorizationHeader,TransactionDTO transactionDTO) {
        AtomicReference<Float> price = new AtomicReference<>(0F);
        String userEmail;
        Optional<Long> userId;
        if(authorizationHeader!=null){
            userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
            userId = userService.findByEmail(userEmail).map(AppUser::getId);
            transactionDTO.setUserId(userId.get());
            transactionDTO.setStatus(TransactionStatus.CANCELED.name());
        }
        transactionDTO.getProducts().forEach(product -> price.set(Float.valueOf(String.format("%.2f",price.get() + (this.productService.findById(product.id()).map(ProductDTO::price).get() * product.quantity())))));
        if(transactionDTO.getStatus() == null) transactionDTO.setStatus(TransactionStatus.CANCELED.name());
        transactionDTO.setPrice(price.toString());
        return Optional.of(mapToTransactionDTO(transactionRepository.save(mapToTransaction(transactionDTO))));
    }

    private static Transaction mapToTransaction(TransactionDTO transactionDTO) {
        return new Transaction(transactionDTO.getId(), transactionDTO.getUserId(), transactionDTO.getCreatedAt(),
                transactionDTO.getProducts(), TransactionStatus.valueOf(transactionDTO.getStatus()),
                Float.valueOf(transactionDTO.getPrice()));
    }

    private static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getUserId(), transaction.getCreatedAt(),
                transaction.getCart(),transaction.getStatus().name(),transaction.getPrice().toString());

    }

    private static PageableTransactionsDTO mapToPageableTransactionDTO(Page<Transaction> transactions) {

        var transactionToSend = transactions.stream().map(TransactionServiceImpl::mapToTransactionDTO).toList();
        return new PageableTransactionsDTO(transactionToSend, transactions.getTotalElements());
    }

}
