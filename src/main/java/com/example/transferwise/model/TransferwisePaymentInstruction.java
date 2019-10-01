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
    @NonNull
    private String targetCurrency;
    @NonNull
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

}
