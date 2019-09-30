package com.example.transferwise.model.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseTransfer {
    private Integer targetAccount;
    private Integer quote;
    //This is from our app, perhaps from the PaymentInstr?
    private UUID customerTransactionId;
    private TransferwiseTransferDetails transferwiseTransferDetails;
}
