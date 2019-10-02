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
public class TransferwiseRecipientResponse<T extends TransferwiseBankDetails> {
    private Integer id;
    private Integer profile;
    private String acccountHolderName;
    private String currency;
    private String country;
    private TransferwiseRecipientType type;
    private T details;

}
