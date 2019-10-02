package com.example.transferwise.model.recipient.BankDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseUSDBankDetails extends TransferwiseBankDetails {
    private String abartn;
    private String accountNumber;
    private String accountType;
    private TransferwiseBankDetailsAddress address;
    public TransferwiseUSDBankDetails(String abartn, String accountNumber, String accountType,
                                      TransferwiseBankDetailsAddress address, String legalType){
        super(legalType);
        this.abartn = abartn;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.address = address;
    }
}
