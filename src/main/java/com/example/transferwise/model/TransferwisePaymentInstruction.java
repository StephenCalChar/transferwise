package com.example.transferwise.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import javax.money.CurrencyUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwisePaymentInstruction {
    // will this ID be from PaymentInstruction?
    private int id;
    private CurrencyUnit targetCurrency;
    private BigDecimal amount;
    private String accountHolderName;
    private String sortCode;
    private String accountNumber;
}
