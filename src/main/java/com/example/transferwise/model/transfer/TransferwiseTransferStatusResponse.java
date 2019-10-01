package com.example.transferwise.model.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseTransferStatusResponse {
    private String type;
    private String status;
    private String errorCode;
}
