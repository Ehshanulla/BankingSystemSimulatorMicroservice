package com.service.operationservice.withdraw;

import com.document.Transaction;


public interface TransactionServiceForWithdraw {
    Transaction withdraw(String accountId, double amount);
}
