package com.example.transferwise;

import com.example.transferwise.model.TransferwisePaymentInstruction;
import com.example.transferwise.model.quote.SubmitQuoteException;
import com.example.transferwise.model.recipient.TransferwiseAddRecipientException;
import com.example.transferwise.model.recipient.TransferwiseCurrencyException;
import com.example.transferwise.model.transfer.TransferwiseFundTransferException;
import com.example.transferwise.model.transfer.TransferwiseTransferResponse;
import com.example.transferwise.model.transfer.TransferwiseTransferStatusResponse;
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
        try {
            TransferwiseTransferStatusResponse transferStatusResponse = transferwiseClient.payInstruction(transferwisePaymentInstruction);
            return new ResponseEntity<TransferwiseTransferStatusResponse>(transferStatusResponse, HttpStatus.OK);
        } catch (TransferwiseAddRecipientException e) {
            // send back to kafka error with details??
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SubmitQuoteException e) {
            // send back details to kafka?
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (TransferwiseFundTransferException e) {
            // Either fund account or send back to Kafka that it has failed
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (TransferwiseCurrencyException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // unrecognised currency
        }

    }
}
