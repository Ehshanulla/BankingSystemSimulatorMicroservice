package com.service.accountmanager;


import com.bean.Account;
import org.springframework.stereotype.Service;

@Service
public class DefaultAccountBalanceManager implements AccountBalanceManager {
    @Override
    public void credit(Account acc, double amount){ acc.setBalance(acc.getBalance() + amount); }
    @Override
    public void debit(Account acc, double amount){ acc.setBalance(acc.getBalance() - amount); }
}
