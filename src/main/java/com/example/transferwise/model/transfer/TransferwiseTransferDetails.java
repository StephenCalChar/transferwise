package com.example.transferwise.model.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseTransferDetails {
    // All these should be nullable
    private String reference;
    private String transferPurpose;
    private String sourceOfFunds;
}
