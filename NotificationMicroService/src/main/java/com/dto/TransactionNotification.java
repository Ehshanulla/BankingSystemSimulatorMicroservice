package com.dto;


public class TransactionNotification {
    private String sourceAccountId;
    private String destAccountId;
    private double amount;
    private String status;
    private String type;

    public TransactionNotification() {
    }

    public TransactionNotification(String sourceAccountId, String destAccountId, double amount, String status, String type) {
        this.sourceAccountId = sourceAccountId;
        this.destAccountId = destAccountId;
        this.amount = amount;
        this.status = status;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getDestAccountId() {
        return destAccountId;
    }

    public void setDestAccountId(String destAccountId) {
        this.destAccountId = destAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TransactionNotification{" +
                "sourceAccountId='" + sourceAccountId + '\'' +
                ", destAccountId='" + destAccountId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

