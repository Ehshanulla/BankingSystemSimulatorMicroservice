package com.service.factory;

import com.document.Transaction;
import com.enums.TransactionType;

public interface TransactionFactoryDepositAndWithdraw {
    Transaction create(TransactionType type, String src, double amount);
}
