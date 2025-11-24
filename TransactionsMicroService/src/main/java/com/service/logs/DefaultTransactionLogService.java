package com.service.logs;

import com.document.Transaction;
import com.repository.TransactionsRepository;
import com.service.factory.TxnSequenceService;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionLogService implements TransactionLogService {

    private final TransactionsRepository txnRepo;

    public DefaultTransactionLogService(TransactionsRepository txnRepo) {
        this.txnRepo = txnRepo;
    }

    @Override
    public Transaction save(Transaction t) {

        if (txnRepo != null) {
            return txnRepo.save(t);
        } else {
            return t;
        }
    }
}
