package com.service.factory;

import com.document.Transaction;
import com.enums.TransactionType;

public interface TransactionFactoryForTransfer {
    Transaction create(TransactionType type, String src, String dest, double amount);
}
