package com.service;

import com.bean.Transaction;
import com.document.Account;
import com.exceptions.AccountNotFoundException;
import com.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    public AccountServiceImpl(AccountRepository accountRepo){ this.accountRepo = accountRepo; }


    @Override
    public Account createAccount(String name){
        Account a = new Account();
        a.setAccountNumber(generateAccountNumber(name));
        a.setHolderName(name);
        a.setCreatedAt(LocalDateTime.now());
        a.setBalance(0);
        a.setStatus("ACTIVE");
        log.debug("Fetching account {}", a.getAccountNumber());
        return accountRepo.save(a);
    }

    @Override
    public Account getAccount(String accountNumber){
        return accountRepo.findByAccountNumber(accountNumber).orElseThrow(()->new AccountNotFoundException("Account not found"));
    }
    @Override
    public List<Transaction> getAllTransactions(String accountId){
        return accountRepo.findByAccountNumber(accountId).map(Account::getTransactions).orElseGet(Collections::emptyList);
    }
    private String generateAccountNumber(String holderName) {
        String prefix = holderName.length() < 3 ? holderName.toUpperCase() : holderName.substring(0,3).toUpperCase(); int randomNum = (int)(Math.random()*9000)+1000; return prefix + randomNum;
    }

    @Override
    public Account updateAccount(Account account) {

        Account existing = accountRepo.findByAccountNumber(account.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!existing.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Account is not active");
        }

        if (account.getHolderName() != null) {
            existing.setHolderName(account.getHolderName());
        }

        if (account.getBalance() != 0) {
            existing.setBalance(account.getBalance());
        }

        if (account.getTransactions() != null && !account.getTransactions().isEmpty()) {
            existing.getTransactions().addAll(account.getTransactions());
        }

        return accountRepo.save(existing);
    }


    @Override
    public boolean deleteAccount(String accountNumber) {
        Optional<Account> acc = accountRepo.findByAccountNumber(accountNumber);
        if(acc.isPresent()){
            Account accFromDB = acc.get();
            accFromDB.setHolderName(null);
            accFromDB.setStatus("INACTIVE");
            accFromDB.setBalance(0);
            accountRepo.save(accFromDB);
            return true;
        }
        return false;
    }
}

