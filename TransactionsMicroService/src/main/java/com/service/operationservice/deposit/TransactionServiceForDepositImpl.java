package com.service.operationservice.deposit;

import com.document.Transaction;
import com.enums.TransactionType;
import com.service.factory.DepositTransactionFactory;
import com.service.factory.TransactionFactoryDepositAndWithdraw;
import com.service.processor.DepositProcessor;
import com.service.processor.TransactionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceForDepositImpl implements TransactionServiceForDeposit {
    private final TransactionFactoryDepositAndWithdraw factory;
    private final TransactionProcessor processor;

    public TransactionServiceForDepositImpl(DepositTransactionFactory factory, DepositProcessor depositProcessor){
        this.factory = factory;
        this.processor=depositProcessor;
    }

    @Override
    public Transaction deposit(String from, double amount){
        Transaction t = factory.create(TransactionType.DEPOSIT, from, amount);
        return processor.process(t);
    }
}
