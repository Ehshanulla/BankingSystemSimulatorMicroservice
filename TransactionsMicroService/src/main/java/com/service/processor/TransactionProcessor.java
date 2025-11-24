package com.service.processor;

import com.document.Transaction;
import com.enums.TransactionType;

public interface TransactionProcessor {
    TransactionType type();
    Transaction process(Transaction t);
}
