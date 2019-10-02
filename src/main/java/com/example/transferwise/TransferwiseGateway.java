package com.example.transferwise;

import com.example.transferwise.model.TransferwisePaymentInstruction;
import com.example.transferwise.model.transfer.TransferwiseTransferResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.validation.Valid;

@RestController
@Getter
@Setter
public class TransferwiseGateway {

    @Autowired
    private TransferwiseClient transferwiseClient;

    @GetMapping({"/test"})
    public ResponseEntity getExpansions() {
        List<TransferwiseTransferResponse> completedTransfers = transferwiseClient.getCompletedTransfers();
        List<TransferwiseTransferResponse> incompletedTransfers = transferwiseClient.getIncompleteTransfers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping({"/submit"})
    public ResponseEntity submitPayment(@RequestBody @Valid TransferwisePaymentInstruction transferwisePaymentInstruction) {
        System.out.println(transferwisePaymentInstruction);
        try {
            transferwiseClient.payInstruction(transferwisePaymentInstruction);
            return new ResponseEntity<>( HttpStatus.OK);
            //change this.
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
