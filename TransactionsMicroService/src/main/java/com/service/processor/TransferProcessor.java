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
public class TransferProcessor implements TransactionProcessor {

    private final RestTemplate restTemplate;

    private final DefaultTransactionValidator validator;
    private final DefaultTransactionLogService logService;
    private final AccountBalanceManager balanceManager;
    private static final Logger log = LoggerFactory.getLogger(TransferProcessor.class);
    public TransferProcessor(RestTemplate r, DefaultTransactionValidator v, DefaultTransactionLogService l, AccountBalanceManager m){
        this.restTemplate = r;
        this.validator=v;
        this.logService=l;
        this.balanceManager=m;
    }
    @Override
    public TransactionType type(){ return TransactionType.TRANSFER; }
    @Override
    @CircuitBreaker(name = "transactionService", fallbackMethod = "fallbackTransfer")
    public Transaction process(Transaction t){
        validator.validate(t);

        Account src = restTemplate.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/"+t.getSourceAccountId(),Account.class);
        Account dst = restTemplate.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/"+t.getDestinationAccountId(),Account.class);
        assert src != null;
        if (src.getBalance() < t.getAmount()){
            t.setStatus(TransactionStatus.FAILED);
            logService.save(t);
            src.getTransactions().add(t);
            assert dst != null;
            dst.getTransactions().add(t);
            restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",src);
            restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",dst);
            throw new InsufficientBalanceException("Insufficient funds");
        }
        balanceManager.debit(src, t.getAmount());
        balanceManager.credit(dst, t.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        logService.save(t);
        log.info("Processing {} of {} for account {}", t.getType(), t.getAmount(), t.getSourceAccountId());
        src.getTransactions().add(t);
        assert dst != null;
        dst.getTransactions().add(t);
        restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",src);
        restTemplate.put("http://ACCOUNTMICROSERVICE/api/accounts/update",dst);
        return t;
    }

    public Transaction fallbackTransfer(Transaction t, Throwable ex) {
        t.setStatus(TransactionStatus.FAILED);
        log.warn("Fallback triggered for transaction {} due to {}", t.getSourceAccountId(), ex.getMessage());
        logService.save(t);
        return t;
    }
}
