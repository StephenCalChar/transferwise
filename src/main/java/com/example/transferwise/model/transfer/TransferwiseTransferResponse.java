package com.example.transferwise.model.transfer;

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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseTransferResponse {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Integer id;
    private Integer user;
    private Integer targetAccount;
    private Integer sourceAccount;
    private Integer quote;
    private String status;
    private String reference;
    private BigDecimal rate;
    @JsonDeserialize(using = TransferwiseTransferResponse.DateDeserializer.class)
    private LocalDateTime created;
    private Integer business;
    private Integer transferRequest;
    private TransferwiseTransferDetails details;
    private Boolean hasActiveIssues;
    private String sourceCurrency;
    private BigDecimal sourceValue;
    private String targetCurrency;
    private BigDecimal targetValue;
    private UUID customerTransactionId;

    public static class DateDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return LocalDateTime.from(DATE_FORMATTER.parse(text));
        }
    }
}
