package com.example.transferwise;

import com.example.transferwise.model.TransferwisePaymentInstruction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

@RestController
@Getter
@Setter
public class TransferwiseGateway {

    @Autowired
    private TransferwiseClient transferwiseClient;

    @GetMapping({"/test"})
    public ResponseEntity<Integer> getExpansions() {
        CurrencyUnit usd = Monetary.getCurrency("GBP");
        TransferwisePaymentInstruction transferwisePaymentInstruction = new TransferwisePaymentInstruction(
                45
                , usd
                , new BigDecimal("500.45")
                , "Ann Johnson"
                //ensure this is mapped over with no dashes
                , "231470"
                , "28821822"
        );
        transferwiseClient.payInstruction(transferwisePaymentInstruction);
        return new ResponseEntity<>(36, HttpStatus.OK);
    }
}
