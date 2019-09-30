package com.example.transferwise;

import com.example.transferwise.model.*;
import com.example.transferwise.model.balance.TransferwiseCheckBalanceResponse;
import com.example.transferwise.model.quote.TransferWiseQuoteResponse;
import com.example.transferwise.model.quote.TransferwiseQuote;
import com.example.transferwise.model.quote.TransferwiseQuoteType;
import com.example.transferwise.model.recipient.TransferwiseGBPBankDetails;
import com.example.transferwise.model.recipient.TransferwiseRecipient;
import com.example.transferwise.model.recipient.TransferwiseRecipientResponse;
import com.example.transferwise.model.recipient.TransferwiseRecipientType;
import com.example.transferwise.model.transfer.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestOperations;

import java.util.UUID;

@Getter
@Setter
public class TransferwiseClient {

    private TransferwiseProfile transferwiseProfile;
    private TransferwiseApi transferwiseApi;

    public TransferwiseClient(RestOperations restOperations) {
        this.transferwiseApi = new TransferwiseApi(restOperations);
        this.transferwiseProfile = this.transferwiseApi.getTransferwiseProfile();
    }

    public void payInstruction(TransferwisePaymentInstruction paymentInstruction) {
        try {
            TransferwiseQuote quote = createQuote(this.transferwiseProfile.getId(), paymentInstruction);
            TransferWiseQuoteResponse quoteResponse = this.submitQuote(quote);
            TransferwiseRecipient recipient = this.createRecipient(this.transferwiseProfile.getId(), paymentInstruction);
            TransferwiseRecipientResponse recipientResponse = this.submitRecipient(recipient);
            TransferwiseTransfer transfer = this.createTransfer(recipientResponse.getId(), quoteResponse.getId(), paymentInstruction);
            TransferwiseTransferResponse transferResponse = this.submitTransfer(transfer);
            TransferwiseTransferStatusResponse fundTransferResponse = fundTransfer(transferResponse.getId(), this.transferwiseProfile.getId());
            TransferwiseTransferStatusResponse transferStatusResponse = this.transferwiseApi.checkTransferStatus(transferResponse.getId());
        } catch(TransferwiseAddRecipientException e){
            // send back to kafka error with details??
        }


    }

    //change to int
    private TransferwiseQuote createQuote(int profileId, TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseQuote(
                profileId
                , "GBP"
                , paymentInstruction.getTargetCurrency().toString()
                , "FIXED"
                // either target amount is required or Source Amount but never both -- I think we want target?
                , paymentInstruction.getAmount()
                , TransferwiseQuoteType.BALANCE_PAYOUT.toString()
        );
    }

    private TransferWiseQuoteResponse submitQuote(TransferwiseQuote transferwiseQuote) {
        return this.transferwiseApi.submitQuote(transferwiseQuote);
    }

    private TransferwiseRecipient createRecipient(int profileId, TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseRecipient(
                paymentInstruction.getTargetCurrency().toString()
                // This will depend on where the payment is going to i.e EU or UK
                , TransferwiseRecipientType.sort_code
                , profileId
                , paymentInstruction.getAccountHolderName()

                , new TransferwiseGBPBankDetails(
                paymentInstruction.getSortCode()
                , paymentInstruction.getAccountNumber()
                // This may change, I am unsure what it means exactly
                , "PRIVATE"
        )
        );
    }

    private TransferwiseRecipientResponse submitRecipient(TransferwiseRecipient transferwiseRecipient) throws TransferwiseAddRecipientException {
        return this.transferwiseApi.addRecipient(transferwiseRecipient);
    }

    private TransferwiseTransfer createTransfer(int targetAccount, int quote, TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseTransfer(
                targetAccount
                , quote
                //
                // Don't know howe we should create that.
                , UUID.randomUUID()
                , new TransferwiseTransferDetails()
        );
    }

    private TransferwiseTransferResponse submitTransfer(TransferwiseTransfer transfer) {
        return this.transferwiseApi.submitTransfer(transfer);
    }

    private TransferwiseTransferStatusResponse fundTransfer(int transferId, int accountId) {
        //maybe check balance or pay in balance here
        TransferwiseCheckBalanceResponse checkBalanceResponse = this.transferwiseApi.checkAccountBalance(accountId);
        return this.transferwiseApi.fundTransfer(transferId);
    }
}
