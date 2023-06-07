package com.web.store.app.backend.payment.process.transaction.service;

import com.web.store.app.backend.payment.process.cart.service.CartProductService;
import com.web.store.app.backend.payment.process.transaction.document.Transaction;
import com.web.store.app.backend.payment.process.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.payment.process.transaction.dto.TransactionDTO;
import com.web.store.app.backend.payment.process.transaction.model.TransactionStatus;
import com.web.store.app.backend.payment.process.transaction.repository.TransactionRepository;
import com.web.store.app.backend.product.service.ProductService;
import com.web.store.app.backend.security.service.JwtService;
import com.web.store.app.backend.user.entity.AppUser;
import com.web.store.app.backend.user.service.AppUserService;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;
    private final AppUserService userService;
    private final ElasticsearchOperations operations;
    private final CartProductService cartProductService;
    private final ProductService productService;

    public Optional<PageableTransactionsDTO> findUserTransactions(String authorizationHeader, Integer page, Integer size) {

        final var userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        var userid = userService.findByEmail(userEmail).map(AppUser::getId);

        return Optional.of(transactionRepository.findTransactionsByUserId(userid.get(), PageRequest.of(page, size)))
                .map(TransactionServiceImpl::mapToPageableTransactionDTO);
    }

    public Optional<TransactionDTO> findTransactionByCartId(String cartId) {


        return Optional.of(transactionRepository.findTransactionsByCartId(cartId))
                .map(TransactionServiceImpl::mapToTransactionDTO);
    }

    public Optional<TransactionDTO> saveTransaction(String authorizationHeader, TransactionDTO transactionDTO) {
        Random random = new Random();
        var statuses = TransactionStatus.values();
        String userEmail;
        Optional<Long> userId;
        if (authorizationHeader != null) {
            userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
            userId = userService.findByEmail(userEmail).map(AppUser::getId);
            if(userId.isEmpty()) return Optional.empty();
            transactionDTO.setUserId(userId.get());
        }
        transactionDTO.setStatus(statuses[random.nextInt(statuses.length)].name());
        Float price = 0F;
        for (int i = 0; i < transactionDTO.getCartProduct().size(); i++) {
            var quantity = transactionDTO.getCartProduct().get(i).getQuantity();
            int finalI = i;
            var productPrice = productService.findById(transactionDTO.getCartProduct().get(i).getProductId())
                    .stream().filter(productDTO -> productDTO.id()
                            .contains(transactionDTO.getCartProduct().get(finalI).getProductId()))
                    .findFirst().get().price();
            price += productPrice * quantity;
            if(!TransactionStatus.valueOf(transactionDTO.getStatus())
                    .equals(TransactionStatus.CANCELED)) productService.changeStock(transactionDTO.getCartProduct()
                    .get(i).getProductId(),quantity);
        }
        transactionDTO.setPrice(String.format("%.2f",price));
        transactionDTO.setCartId(cartProductService.saveCartProductBulk(transactionDTO.getCartProduct()));


        return Optional.of(mapToTransactionDTO(transactionRepository.save(mapToTransaction(transactionDTO))));
    }
    @Override
    public PageableTransactionsDTO findTransactionsByBrand(String brand, Integer page, Integer size) {
        if (brand.isBlank()) return null;

        var query = "{\"nested\":{\"path\":\"cart\",\"query\":{\"match\":{\"car.brand\":\"" + brand + "\"}}}}";
        var stringQuery = new StringQuery(query);
        var searchHits = operations.search(stringQuery, Transaction.class, IndexCoordinates.of("transaction"));
        var transactionDto = searchHits.stream().map(SearchHit::getContent)
                .map(TransactionServiceImpl::mapToTransactionDTO).toList();
        return new PageableTransactionsDTO(transactionDto, (long) (Math.ceil(((double) (searchHits.getTotalHits() / size)))));

    }

    private static Transaction mapToTransaction(TransactionDTO transactionDTO) {
        return new Transaction(null, transactionDTO.getUserId(), transactionDTO.getCreatedAt(),
                transactionDTO.getCartId(), transactionDTO.getStatus(),
                Float.valueOf(transactionDTO.getPrice()));
    }

    private static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getUserId(), transaction.getCreatedAt(),
                transaction.getCartId(), transaction.getStatus(), transaction.getPrice().toString(),
                null);

    }

    private static PageableTransactionsDTO mapToPageableTransactionDTO(Page<Transaction> transactions) {
        var transactionToSend = transactions.stream().map(TransactionServiceImpl::mapToTransactionDTO).toList();
        return new PageableTransactionsDTO(transactionToSend, transactions.getTotalElements());
    }

}
