package com.example.transferwise.model.recipient.BankDetails;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseBankDetailsAddress {
    private String country;
    private String city;
    private String postCode;
    private String firstLine;
}
