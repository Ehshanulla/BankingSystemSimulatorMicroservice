package com.dto.requests;

import jakarta.validation.constraints.Positive;

public class DepositRequest {

    @Positive(message = "Amount must be positive")
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
