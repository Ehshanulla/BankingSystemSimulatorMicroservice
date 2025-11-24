package com.dto.requests;

import jakarta.validation.constraints.NotBlank;

public class AccountCreateRequest {

    @NotBlank(message = "Holder name cannot be empty")
    private String holderName;


    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
}

