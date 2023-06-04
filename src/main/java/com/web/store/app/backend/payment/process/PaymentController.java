package com.web.store.app.backend.payment.process;

import com.web.store.app.backend.payment.process.cart.dto.CartProductDto;
import com.web.store.app.backend.payment.process.cart.service.CartProductService;
import com.web.store.app.backend.payment.process.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.payment.process.transaction.dto.TransactionDTO;
import com.web.store.app.backend.payment.process.transaction.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment/api")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    
    public final TransactionService transactionService;
    public final CartProductService cartProductService;

    @GetMapping("/private/transaction/find/all")
    private ResponseEntity<PageableTransactionsDTO> getAllTransactionsForUser(final Integer page,
                                                                              @RequestParam(defaultValue = "10") final Integer size,
                                                                              final HttpServletRequest request) {

        return transactionService.findUserTransactions(request.getHeader(HttpHeaders.AUTHORIZATION),page, size)
                .map(transactions -> ResponseEntity.status(HttpStatus.OK)
                        .body(transactions))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @GetMapping("/private/cart/find/{cartId}")
    private ResponseEntity<List<CartProductDto>> getAllCartProductsForUser(@PathVariable String cartId) {

        return Optional.of(cartProductService.getCartsByCartId(cartId))
                .map(transactions -> ResponseEntity.status(HttpStatus.OK)
                        .body(transactions))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @PostMapping(value = "/public/transaction/add")
    private ResponseEntity<TransactionDTO> saveTransaction(@RequestBody TransactionDTO transactionDTO) {

        return transactionService.saveTransaction(null,transactionDTO).map(
                transaction -> ResponseEntity.status(HttpStatus.CREATED).body(transaction)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }
    @PostMapping(value = "/private/transaction/add")
    private ResponseEntity<TransactionDTO> saveTransaction(@RequestBody TransactionDTO transactionDTO,
                                                           final HttpServletRequest request) {

        return transactionService.saveTransaction(request.getHeader(HttpHeaders.AUTHORIZATION),transactionDTO).map(
                transaction -> ResponseEntity.status(HttpStatus.CREATED).body(transaction)).orElseGet(
                () -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private Map<String, String> handleExceptions(
            Exception ex) {
        var errors = new HashMap<String, String>();
        var fieldName = ex.getClass().getSimpleName();
        var errorMessage = ex.getMessage();
        errors.put(fieldName, errorMessage);

        return errors;
    }
}
