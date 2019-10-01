package com.example.transferwise.model.recipient.BankDetails;

import com.example.transferwise.model.recipient.BankDetails.TransferwiseBankDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseEURBankDetails extends TransferwiseBankDetails {
    private String IBAN;
    public TransferwiseEURBankDetails(String IBAN, String legalType){
        super(legalType);
        this.IBAN=IBAN;
    }
}
