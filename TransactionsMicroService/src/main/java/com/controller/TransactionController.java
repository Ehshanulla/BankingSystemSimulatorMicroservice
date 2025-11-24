package com.controller;


import com.document.Transaction;

import com.dto.requests.DepositRequest;
import com.dto.requests.TransferRequest;
import com.dto.requests.WithdrawRequest;
import com.dto.responses.TransactionResponse;

import com.service.kafkaservices.TransactionKafkaService;
import com.service.kafkaservices.TransactionKafkaServiceImpl;
import com.service.operationservice.deposit.TransactionServiceForDeposit;
import com.service.operationservice.transfer.TransactionServiceForTransfer;
import com.service.operationservice.withdraw.TransactionServiceForWithdraw;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionServiceForDeposit depositService;
    private final TransactionServiceForWithdraw withdrawService;
    private final TransactionServiceForTransfer transferService;
    private final TransactionKafkaService transactionKafkaService;

    public TransactionController(TransactionServiceForDeposit depositService, TransactionServiceForWithdraw withdrawService, TransactionServiceForTransfer transferService, TransactionKafkaService transactionKafkaService) {
        this.depositService = depositService;
        this.withdrawService = withdrawService;
        this.transferService = transferService;
        this.transactionKafkaService = transactionKafkaService;
    }


    @PutMapping(value = "/{accountNumber}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> deposit(@Valid @PathVariable String accountNumber,@Valid @RequestBody DepositRequest request) {
        Transaction t = depositService.deposit(accountNumber, request.getAmount());
        transactionKafkaService.sendTransaction(t);
        return ResponseEntity.ok(TransactionResponse.from(t));
    }


    @PutMapping(
            value = "/{accountNumber}/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TransactionResponse> withdraw(@Valid @PathVariable String accountNumber,@Valid @RequestBody WithdrawRequest request) {
        Transaction t = withdrawService.withdraw(accountNumber, request.getAmount());
        transactionKafkaService.sendTransaction(t);
        return ResponseEntity.ok(TransactionResponse.from(t));
    }


    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        Transaction t = transferService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
        transactionKafkaService.sendTransaction(t);
        return ResponseEntity.status(HttpStatus.OK).body(TransactionResponse.from(t));
    }
}


