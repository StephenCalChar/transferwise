package com.example.transferwise;

import com.example.transferwise.model.*;
import com.example.transferwise.model.balance.TransferwiseAccountBalance;
import com.example.transferwise.model.balance.TransferwiseCheckBalanceResponse;
import com.example.transferwise.model.quote.SubmitQuoteException;
import com.example.transferwise.model.quote.TransferWiseQuoteResponse;
import com.example.transferwise.model.quote.TransferwiseQuote;
import com.example.transferwise.model.quote.TransferwiseQuoteType;
import com.example.transferwise.model.recipient.*;
import com.example.transferwise.model.recipient.BankDetails.*;
import com.example.transferwise.model.transfer.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestOperations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.money.CurrencyUnit;

@Getter
@Setter
public class TransferwiseClient {
    final private static String GBP_CURRENCY_CODE = "GBP";
    final private static String EUR_CURRENCY_CODE = "EUR";
    final private static String USD_CURRENCY_CODE = "USD";

    private static final String INCOMING_PAYMENT_WAITING_STATUS = "incoming_payment_waiting";
    private static final String PROCESSING_STATUS = "processing";
    private static final String FUNDS_CONVERTED_STATUS = "funds_converted";
    private static final String OUTGOING_PAYMENT_SENT_STATUS = "outgoing_payment_sent";
    private static final String CANCELLED_PAYMENT_STATUS = "cancelled";
    private static final String FUNDS_REFUNDED_STATUS = "funds_refunded";
    private static final String BOUNCED_BACK_STATUS = "bounced_back";

    // Add this through environment variables
    private final int PROFILE_ID = 0;
    private TransferwiseApi transferwiseApi;

    public TransferwiseClient(RestOperations restOperations) {
        this.transferwiseApi = new TransferwiseApi(restOperations);
    }

    // Do you want to return the transfer status from here? Maybe make enum.
    public TransferwiseTransferStatusResponse payInstruction(TransferwisePaymentInstruction paymentInstruction) throws TransferwiseCurrencyException, TransferwiseAddRecipientException, SubmitQuoteException, TransferwiseFundTransferException {
        // Its possible we want to just set up the payments in the first instance and then have another
        // option for them all to be paid once their is definitely sufficient funds in the account.
        // I think there should also be logic to check whether a recipient exists or not.
        TransferwiseRecipient recipient = this.createRecipient(PROFILE_ID, paymentInstruction);
        TransferwiseRecipientResponse recipientResponse = this.submitRecipient(recipient);
        TransferwiseQuote quote = createQuote(PROFILE_ID, paymentInstruction);
        TransferWiseQuoteResponse quoteResponse = this.submitQuote(quote);
        TransferwiseTransfer transfer = this.createTransfer(recipientResponse.getId(), quoteResponse.getId(), paymentInstruction);
        TransferwiseTransferResponse transferResponse = this.submitTransfer(transfer);
        TransferwiseTransferStatusResponse fundTransferResponse = this.fundTransfer(transferResponse.getId(), PROFILE_ID, quote);
        return this.transferwiseApi.checkTransferStatus(transferResponse.getId());
    }

    private TransferwiseQuote createQuote(int profileId, TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseQuote(
                profileId
                , "GBP"
                , paymentInstruction.getTargetCurrency()
                , "FIXED"
                // either target amount is required or Source Amount but never both -- I think we want target?
                , paymentInstruction.getAmount()
                , TransferwiseQuoteType.BALANCE_PAYOUT.toString()
        );
    }

    private TransferWiseQuoteResponse submitQuote(TransferwiseQuote transferwiseQuote) throws SubmitQuoteException {
        return this.transferwiseApi.submitQuote(transferwiseQuote);
    }

    private TransferwiseRecipient createRecipient(int profileId, TransferwisePaymentInstruction paymentInstruction) throws TransferwiseCurrencyException {
        switch (paymentInstruction.getTargetCurrency()) {
            case GBP_CURRENCY_CODE:
                return new TransferwiseRecipient<>(
                        paymentInstruction.getTargetCurrency()
                        , TransferwiseRecipientType.sort_code
                        , profileId
                        , paymentInstruction.getAccountHolderName()
                        , this.createBankGBPDetails(paymentInstruction)
                );
            case EUR_CURRENCY_CODE:
                return new TransferwiseRecipient<>(
                        paymentInstruction.getTargetCurrency()
                        , TransferwiseRecipientType.iban
                        , profileId
                        , paymentInstruction.getAccountHolderName()
                        , this.createBankEURDetails(paymentInstruction)
                );
            case USD_CURRENCY_CODE:
                return new TransferwiseRecipient<>(
                        paymentInstruction.getTargetCurrency()
                        , TransferwiseRecipientType.aba
                        , profileId
                        , paymentInstruction.getAccountHolderName()
                        , this.createBankUSDDetails(paymentInstruction)
                );
            default:
                throw new TransferwiseCurrencyException("Currency Not Supported");
        }
    }

    private TransferwiseRecipientResponse submitRecipient(TransferwiseRecipient transferwiseRecipient) throws TransferwiseAddRecipientException {
        return transferwiseApi.addRecipient(transferwiseRecipient);
    }

    private TransferwiseTransfer createTransfer(int targetAccount, int quote, TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseTransfer(
                targetAccount
                , quote
                // Don't know howe we should create that.
                , UUID.randomUUID()
                , new TransferwiseTransferDetails()
        );
    }

    private TransferwiseTransferResponse submitTransfer(TransferwiseTransfer transfer) {
        return this.transferwiseApi.submitTransfer(transfer);
    }

    private TransferwiseTransferStatusResponse fundTransfer(int transferId, int accountId, TransferwiseQuote quote) throws TransferwiseFundTransferException {
        //maybe check balance or pay in balance here
//        if(this.checkBalanceByCurrency(quote.getTarget(), this.PROFILE_ID).compareTo(quote.getTargetAmount())>= 0){
//            return this.transferwiseApi.fundTransfer(transferId);
//        }
        return this.transferwiseApi.fundTransfer(transferId);
        //throw new TransferwiseFundTransferException("Insufficient Funds");
    }

    BigDecimal checkBalanceByCurrency(String currency, int accountId) throws TransferwiseCurrencyException {
        TransferwiseCheckBalanceResponse checkBalanceResponse = this.transferwiseApi.checkAccountBalance(accountId);
        return checkBalanceResponse.getBalances().stream()
                .filter(balance -> balance.getCurrency().equals(currency))
                .map(balanceResponse-> balanceResponse.getAmount().getValue())
                .findFirst()
                .orElseThrow(()->new TransferwiseCurrencyException("No Account setup for this Currency"));
    }

    private TransferwiseBankDetails createBankGBPDetails(TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseGBPBankDetails(
                paymentInstruction.getSortCode()
                , paymentInstruction.getAccountNumber()
                , "BUSINESS"
        );
    }

    private TransferwiseBankDetails createBankUSDDetails(TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseUSDBankDetails(
                paymentInstruction.getAbartn()
                , paymentInstruction.getAccountNumber()
                , "CHECKING"
                , new TransferwiseBankDetailsAddress(
                paymentInstruction.getCountry()
                , paymentInstruction.getCity()
                , paymentInstruction.getPostCode()
                , paymentInstruction.getFirstLine())
                , "PRIVATE"
        );
    }

    private TransferwiseBankDetails createBankEURDetails(TransferwisePaymentInstruction paymentInstruction) {
        return new TransferwiseEURBankDetails(
                paymentInstruction.getIBAN()
                , "BUSINESS"
        );
    }

    public List<TransferwiseTransferResponse> getIncompleteTransfers() {
        return this.transferwiseApi.getTransfersByStatus(
                PROFILE_ID
                , INCOMING_PAYMENT_WAITING_STATUS
                , PROCESSING_STATUS
                , FUNDS_CONVERTED_STATUS
                , FUNDS_REFUNDED_STATUS
                , BOUNCED_BACK_STATUS
        );
    }

    public List<TransferwiseTransferResponse> getCompletedTransfers() {
        return this.transferwiseApi.getTransfersByStatus(PROFILE_ID, OUTGOING_PAYMENT_SENT_STATUS);
    }

}
