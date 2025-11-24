package com.dto.responses;


import com.bean.Transaction;

public class TransactionResponse {

    private String transactionId;
    private String type;
    private double amount;
    private String timestamp;
    private String status;
    private String sourceAccount;
    private String destinationAccount;

    public static TransactionResponse from(Transaction t) {
        TransactionResponse res = new TransactionResponse();

        res.transactionId = t.getTransactionId();
        res.type = t.getType().name();
        res.amount = t.getAmount();
        res.status = t.getStatus().name();
        res.sourceAccount = t.getSourceAccountId();
        res.destinationAccount = t.getDestinationAccountId();

        if (t.getTimestamp() != null)
            res.timestamp = t.getTimestamp().toString();

        return res;
    }

    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
}

