package com.service;


import com.bean.Transaction;
import com.document.Account;

import java.io.StringReader;
import java.util.List;


public interface AccountService {
    Account createAccount(String name);
    Account getAccount(String accountNumber);
    List<Transaction> getAllTransactions(String accountId);
    Account updateAccountBalance(String accountNumber, double balance);
    Account updateAccount(Account account);
    boolean deleteAccount(String accountNumber);
}
