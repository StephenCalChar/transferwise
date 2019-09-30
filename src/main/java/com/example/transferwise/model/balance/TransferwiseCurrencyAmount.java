package com.example.transferwise.model.balance;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferwiseCurrencyAmount {
    private BigDecimal value;
    private String currency;
}
