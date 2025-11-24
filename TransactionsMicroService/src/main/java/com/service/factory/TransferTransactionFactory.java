package com.service.factory;

import com.document.Transaction;
import com.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferTransactionFactory implements TransactionFactoryForTransfer {
    private final TxnSequenceService seqService;

    public TransferTransactionFactory(TxnSequenceService seqService) {
        this.seqService = seqService;
    }

    @Override
    public Transaction create(TransactionType type, String src, String dest, double amount) {
        Transaction t = new Transaction();
        String txnId = generateTransactionId();
        t.setTransactionId(txnId);
        t.setType(type);
        t.setAmount(amount);
        t.setTimestamp(LocalDateTime.now());
        t.setSourceAccountId(src);
        t.setDestinationAccountId(dest);
        return t;
    }

    private synchronized String generateTransactionId() {
        int seq = seqService.nextSequence();
        String date = java.time.LocalDate.now().toString().replace("-", "");
        return "TXN-" + date + "-" + String.format("%03d", seq);
    }
}
