package com.example.transferwise.model.recipient;

import com.example.transferwise.model.recipient.BankDetails.TransferwiseBankDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseRecipient<T extends  TransferwiseBankDetails>  {
    private String currency;
    private TransferwiseRecipientType type;
    private Integer profile;
    private String accountHolderName;
    // this will need either an interface or inheritance further down the line as can change depending on region
    private T details;
}
