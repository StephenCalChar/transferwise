package com.example.transferwise.model.transfer;

import lombok.Getter;

@Getter
public class TransferwiseFundTransfer {
    private String type;
    public TransferwiseFundTransfer(){
        this.type="BALANCE";
    }
}
