package com.example.transferwise.model.recipient.BankDetails;

import com.example.transferwise.model.recipient.BankDetails.TransferwiseBankDetails;
import lombok.*;

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
