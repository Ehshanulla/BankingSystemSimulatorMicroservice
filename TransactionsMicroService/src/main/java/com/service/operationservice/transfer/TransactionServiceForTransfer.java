package com.service.operationservice.transfer;

import com.document.Transaction;


public interface TransactionServiceForTransfer {
    Transaction transfer(String from, String to, double amount);
}
