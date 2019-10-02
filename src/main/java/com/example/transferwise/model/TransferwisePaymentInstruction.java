package com.example.transferwise.model;

import lombok.*;

import java.math.BigDecimal;
import javax.money.CurrencyUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwisePaymentInstruction {
    // will this ID be from PaymentInstruction?
    private int id;
    private String targetCurrency;
    private BigDecimal amount;
    private String accountHolderName;
    private String sortCode;
    private String accountNumber;
    private String country;
    private String city;
    private String postCode;
    private String firstLine;
    private String IBAN;
    private String abartn;
    private String accountType;

    public TransferwisePaymentInstruction(int id, String targetCurrency, BigDecimal amount, String accountHolderName,
                                          String sortCode, String accountNumber) {
        this.id=id;
        this.targetCurrency=targetCurrency;
        this.amount=amount;
        this.accountHolderName=accountHolderName;
        this.sortCode=sortCode;
        this.accountNumber=accountNumber;

    }

}
