package com.service;


import com.dto.TransactionNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class NotificationServiceImpl implements NotificationService {



    Queue<String> transactionNotifications = new ConcurrentLinkedQueue<>();;

    @Override
    @KafkaListener(topics = "notification-service",
    groupId = "notification")
    public void createNotification(TransactionNotification tn) {
        if(tn.getStatus().equals("SUCCESS")){
            if(tn.getType().equals("DEPOSIT")){
                transactionNotifications.add(tn.getAmount() +" Amount has been credited to your account "+"Account number is "+tn.getSourceAccountId());
            } else if (tn.getType().equals("WITHDRAW")) {
                transactionNotifications.add(tn.getAmount() +" Amount has been debited from your account "+ "Account number is "+tn.getSourceAccountId());
            }else{
                transactionNotifications.add("Amount has been transferred from your account "+tn.getSourceAccountId() +" to "+tn.getDestAccountId());
            }
        }else {
            transactionNotifications.add("Transaction failed for "+tn.getSourceAccountId());
            if(tn.getType().equals("TRANSFER")){
                transactionNotifications.add("Transaction failed for "+tn.getDestAccountId());
            }
        }
    }

    public Queue<String> getNotifications(){
        return transactionNotifications;
    }

}

