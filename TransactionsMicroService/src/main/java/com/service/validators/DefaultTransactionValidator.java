package com.service.validators;

import com.document.Transaction;
import com.exceptions.InvalidAmountException;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionValidator implements TransactionValidator {
    @Override
    public void validate(Transaction t){
        if (t.getAmount() <= 0) throw new InvalidAmountException("Amount must be positive");
    }
}
