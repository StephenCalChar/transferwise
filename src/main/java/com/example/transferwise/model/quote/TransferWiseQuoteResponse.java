package com.example.transferwise.model.quote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferWiseQuoteResponse {
    //public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private Integer id;
    private String source;
    private String target;
    private BigDecimal targetAmount;
    private BigDecimal sourceAmount;
    private String type;
    private BigDecimal rate;
    private LocalDateTime createdTime;
    private Integer createdByUserId;
    private Integer profile;
    private String rateType;
    private LocalDateTime deliveryEstimate;
    private BigDecimal fee;
    private String[] allowedProfileTypes;



}
