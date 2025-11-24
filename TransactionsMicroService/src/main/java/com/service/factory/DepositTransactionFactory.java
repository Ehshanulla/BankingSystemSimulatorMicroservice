package com.service.factory;

import com.document.Transaction;
import com.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class DepositTransactionFactory implements TransactionFactoryDepositAndWithdraw {
    TxnSequenceService seqService;

    public DepositTransactionFactory(TxnSequenceService seqService) {
        this.seqService = seqService;
    }

    @Override
    public Transaction create(TransactionType type, String src, double amount) {

        Transaction t = new Transaction();
        String txnId = generateTransactionId();
        t.setTransactionId(txnId);
        t.setType(type);
        t.setTimestamp(LocalDateTime.now());
        t.setAmount(amount);
        t.setSourceAccountId(src);
        t.setDestinationAccountId("-");
        return t;
    }

    private synchronized String generateTransactionId() {
        int seq = seqService.nextSequence();
        String date = java.time.LocalDate.now().toString().replace("-", "");
        return "TXN-" + date + "-" + String.format("%03d", seq);
    }
}
