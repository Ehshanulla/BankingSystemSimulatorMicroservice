package com.service.accountmanager;

import com.bean.Account;

public interface AccountBalanceManager {
    void credit(Account acc, double amount);
    void debit(Account acc, double amount);
}
