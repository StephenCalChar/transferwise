package com.example.transferwise.model.quote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseQuote {
    private Integer profile;
    private String source;
    private String target;
    private String rateType;
    // Which of these 2 do we want?
    private BigDecimal targetAmount;
    //private BigDecimal sourceAmount;
    private String type;

}
