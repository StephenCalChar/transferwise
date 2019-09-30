package com.example.transferwise.model.balance;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferwiseAccountBalance {
    private String balanceType;
    private String currency;
    private TransferwiseCurrencyAmount amount;
    private TransferwiseCurrencyAmount reservedAmount;
    // not sure what this is "group" or why we would even need.
    //private bankDetails;
}
