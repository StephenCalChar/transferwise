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
public class TransferwiseCheckBalanceResponse {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    private Integer id;
    private Integer profileId;
    private Integer recipientId;
    @JsonDeserialize(using = TransferwiseCheckBalanceResponse.DateDeserializer.class)
    private LocalDateTime creationTime;
    @JsonDeserialize(using = TransferwiseCheckBalanceResponse.DateDeserializer.class)
    private LocalDateTime modificationTime;
    private Boolean active;
    private Boolean eligible;
    private List<TransferwiseAccountBalance> balances;

    public static class DateDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return LocalDateTime.from(DATE_FORMATTER.parse(text));
        }
    }
}
