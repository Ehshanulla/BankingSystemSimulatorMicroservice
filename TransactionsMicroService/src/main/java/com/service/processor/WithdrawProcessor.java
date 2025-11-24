package com.service.processor;


import com.bean.Account;
import com.document.Transaction;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.exceptions.AccountNotFoundException;
import com.exceptions.InsufficientBalanceException;

import com.service.accountmanager.AccountBalanceManager;
import com.service.logs.DefaultTransactionLogService;
import com.service.validators.DefaultTransactionValidator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WithdrawProcessor implements TransactionProcessor {
    private final RestTemplate restTemplate;
    private final DefaultTransactionValidator validator;
    private final DefaultTransactionLogService logService;
    private final AccountBalanceManager balanceManager;

    private static final Logger log = LoggerFactory.getLogger(WithdrawProcessor.class);

    public WithdrawProcessor(RestTemplate r, DefaultTransactionValidator v, DefaultTransactionLogService l, AccountBalanceManager m){
        this.restTemplate=r;
        this.validator=v;
        this.logService=l;
        this.balanceManager=m;
    }
    @Override
    public TransactionType type(){ return TransactionType.WITHDRAW; }
    @Override
    @CircuitBreaker(name = "transactionService", fallbackMethod = "fallbackWithdraw")
    public Transaction process(Transaction t){
        validator.validate(t);
        Account acc = restTemplate.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/"+t.getSourceAccountId(),Account.class);
        assert acc != null;
        if (acc.getBalance() < t.getAmount()) {
            t.setStatus(TransactionStatus.FAILED);
            logService.save(t);
            acc.getTransactions().add(t);
            restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",acc);

            throw new InsufficientBalanceException("Insufficient funds");
        }
        balanceManager.debit(acc, t.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        logService.save(t);
        log.info("Processing {} of {} for account {}", t.getType(), t.getAmount(), t.getSourceAccountId());
        acc.getTransactions().add(t);
        restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",acc);
        return t;
    }

    public Transaction fallbackWithdraw(Transaction t, Throwable ex) {
        t.setStatus(TransactionStatus.FAILED);
        log.warn("Fallback triggered for transaction {} due to {}", t.getSourceAccountId(), ex.getMessage());
        logService.save(t);
        return t;
    }
}
