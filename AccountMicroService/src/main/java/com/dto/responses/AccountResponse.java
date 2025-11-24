package com.dto.responses;

import com.document.Account;

public class AccountResponse {

    private String accountNumber;
    private String holderName;
    private double balance;
    private String status;
    private String createdAt;

    public static AccountResponse from(Account account) {
        AccountResponse res = new AccountResponse();

        res.accountNumber = account.getAccountNumber();
        res.holderName = account.getHolderName();
        res.balance = account.getBalance();
        res.status = account.getStatus();

        if (account.getCreatedAt() != null)
            res.createdAt = account.getCreatedAt().toString();

        return res;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}

