package com.web.store.app.backend.transaction;

import com.web.store.app.backend.transaction.dto.PageableTransactionsDTO;
import com.web.store.app.backend.transaction.dto.TransactionDTO;
import com.web.store.app.backend.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction/api")
@AllArgsConstructor
@Slf4j
public class TransactionController {
    
    public final TransactionService transactionService;

    @GetMapping("/public/find/user/{userId}")
    private ResponseEntity<PageableTransactionsDTO> getAllTransactionsByUserId(@PathVariable Long userId, final Integer page,
                                                                               @RequestParam(defaultValue = "10") final Integer size) {

        return transactionService.findUserTransactions(userId, page, size)
                .map(transactions -> ResponseEntity.status(HttpStatus.OK)
                        .body(transactions))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }

    @PostMapping(value = "/public/add")
    private ResponseEntity<TransactionDTO> saveTransaction(@RequestBody TransactionDTO transactionDTO) {

        return transactionService.saveTransaction(transactionDTO).map(
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
