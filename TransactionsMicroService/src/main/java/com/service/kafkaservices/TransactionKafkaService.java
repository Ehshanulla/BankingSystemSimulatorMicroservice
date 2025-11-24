package com.service.kafkaservices;

import com.document.Transaction;

public interface TransactionKafkaService {
    void sendTransaction(Transaction t);
}
