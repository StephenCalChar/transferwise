package com.example.transferwise.model.recipient;

import com.example.transferwise.model.recipient.TransferwiseBankDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseGBPBankDetails extends TransferwiseBankDetails {
    private String sortCode;
    private String accountNumber;
    public TransferwiseGBPBankDetails(String sortCode, String accountNumber, String legalType){
        super(legalType);
        this.sortCode= sortCode;
        this.accountNumber=accountNumber;
    }
}
