package com.service.kafkaservices;

import com.document.Transaction;
import com.dto.TransactionNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaServiceImpl implements TransactionKafkaService{

    private final KafkaTemplate<String, TransactionNotification> kafkaTemplate;



    public TransactionKafkaServiceImpl(KafkaTemplate<String, TransactionNotification> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;

    }

    public void sendTransaction(Transaction t){
        String type = t.getType().name();
        TransactionNotification tn = new TransactionNotification(t.getSourceAccountId(),t.getDestinationAccountId(),t.getAmount(),t.getStatus().name(),type);
        kafkaTemplate.send("notification-service",type,tn);
    }

}
