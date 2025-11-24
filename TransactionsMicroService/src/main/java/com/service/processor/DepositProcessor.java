package com.service.processor;

import com.bean.Account;
import com.document.Transaction;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.exceptions.AccountNotFoundException;
import com.service.accountmanager.AccountBalanceManager;
import com.service.logs.DefaultTransactionLogService;
import com.service.validators.DefaultTransactionValidator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepositProcessor implements TransactionProcessor {

    private final RestTemplate restTemplate;
    private final DefaultTransactionLogService logService;
    private final DefaultTransactionValidator validator;
    private final AccountBalanceManager balanceManager;

    private static final Logger log = LoggerFactory.getLogger(DepositProcessor.class);

    public DepositProcessor(RestTemplate r, DefaultTransactionLogService logService, DefaultTransactionValidator validator,
                            AccountBalanceManager balanceManager){
        this.restTemplate = r; this.logService = logService; this.validator = validator; this.balanceManager = balanceManager;
    }

    @Override
    public TransactionType type() {
        return TransactionType.DEPOSIT;
    }

    @Override
    @CircuitBreaker(name = "transactionService", fallbackMethod = "fallbackDeposit")
    public Transaction process(Transaction t) {
        validator.validate(t);
        Account acc = restTemplate.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/"+t.getSourceAccountId(),Account.class);
        balanceManager.credit(acc, t.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        logService.save(t);
        log.info("Processing {} of {} for account {}", t.getType(), t.getAmount(), t.getSourceAccountId());
        assert acc != null;
        acc.getTransactions().add(t);
        restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",acc);
        return t;
    }

    public Transaction fallbackDeposit(Transaction t, Throwable ex) {
        t.setStatus(TransactionStatus.FAILED);
        log.warn("Fallback triggered for transaction {} due to {}", t.getSourceAccountId(), ex.getMessage());
        logService.save(t);
        return t;
    }

}
