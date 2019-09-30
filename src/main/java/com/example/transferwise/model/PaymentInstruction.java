//package com.example.transferwise.model;
//
//import com.example.transferwise.model.core.Clearing;
//import com.example.transferwise.model.core.Settlement;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import lombok.*;
//import org.javamoney.moneta.Money;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import javax.money.CurrencyUnit;
//import javax.money.Monetary;
//
//@Builder(toBuilder = true)
//@AllArgsConstructor(access = AccessLevel.PACKAGE)
//@NoArgsConstructor(access = AccessLevel.PACKAGE)
//@Setter(value = AccessLevel.PACKAGE)
//@Getter
//@ToString
//@EqualsAndHashCode(of = "id")
//public class PaymentInstruction implements Entity, Comparable<PaymentInstruction> {
//
//    public static CurrencyUnit GBP_CURRENCY = Monetary.getCurrency("GBP");
//
//    @NonNull
//    private Recipient recipient;
//    private String batchId;
//    @NonNull
//    @Builder.Default private final LocalDateTime timestamp = LocalDateTime.now();
//    @NonNull
//    @Builder.Default private final UUID id = UUID.randomUUID();
//    @Singular("addRef") @NonNull
//    private Map<String,String> kvRef;
//    private  String statusMessage;
//    @JsonDeserialize(using = MoneyDeserializer.class)
//    @JsonSerialize(using = MoneySerializer.class)
//    @NonNull
//    private Money targetAmount;
//    @NonNull
//    @Builder.Default private final LocalDate latestTargetSettlementDate = LocalDate.now();
//    private LocalDate earliestTargetSettlementDate;
//    @JsonDeserialize(using = CurrencyUnitDeserializer.class)
//    @JsonSerialize(using = CurrencyUnitSerializer.class)
//    @NonNull
//    @Builder.Default private final CurrencyUnit targetSettlementCurrency = GBP_CURRENCY;
//    private String paymentFile;
//    @NonNull
//    @Builder.Default private final PaymentInstructionStatus status = PaymentInstructionStatus.New;
//    @NonNull
//    @Builder.Default private final PaymentChannelType targetChannelType = PaymentChannelType.EFT;
//    @NonNull
//    private String service;
//    @NonNull
//    @Builder.Default private String createdBy = "application";
//
//    private Validation validation;
//    private Routing routing;
//    @Singular("addExecution") @NonNull
//    private List<Execution> executions;
//    private Clearing clearing;
//    private Settlement settlement;
//
//    @Override
//    public byte[] getKey() {
//        return Entity.stringToKey(id.toString());
//    }
//
//    @Override
//    public int compareTo(PaymentInstruction o) {
//        return timestamp.compareTo(o.timestamp);
//    }
//}
