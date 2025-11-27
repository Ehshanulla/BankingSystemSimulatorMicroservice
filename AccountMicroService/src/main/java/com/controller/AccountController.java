package com.controller;

import com.document.Account;
import com.dto.requests.AccountCreateRequest;
import com.dto.responses.AccountResponse;

import com.dto.responses.TransactionResponse;
import com.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {

        Account account = accountService.createAccount(request.getHolderName());
        log.info("Creating account for {}", request.getHolderName());
        return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(account));
    }


    @GetMapping(value = "/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@Valid @PathVariable String accountNumber) {
        Account account = accountService.getAccount(accountNumber);

        return ResponseEntity.ok(AccountResponse.from(account));
    }


    @GetMapping(value = "/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@Valid @PathVariable String accountNumber) {

        List<TransactionResponse> list = accountService.getAllTransactions(accountNumber)
                .stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PutMapping(value = "{accountNumber}/balance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccountBalance(@PathVariable String accountNumber, @RequestBody Account accountReq){
        double balance = accountReq.getBalance();
        Account account = accountService.updateAccountBalance(accountNumber,balance);
        log.info("Updating balance of {}", accountNumber);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping(value = "update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccount(@Valid @RequestBody Account request){
        Account account = accountService.updateAccount(request);
        log.info("Updating account for {}", request.getHolderName());
        return ResponseEntity.ok(AccountResponse.from(account));
    }



    @DeleteMapping(value = "{accountNumber}/status")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        boolean deleted = accountService.deleteAccount(accountNumber);

        if (deleted) {
            log.info("Deleting account for {}", accountNumber);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
