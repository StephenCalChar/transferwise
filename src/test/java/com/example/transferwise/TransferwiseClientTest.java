package com.example.transferwise;


import com.example.transferwise.model.TransferwisePaymentInstruction;
import com.example.transferwise.model.TransferwiseProfile;
import com.example.transferwise.model.quote.SubmitQuoteException;
import com.example.transferwise.model.quote.TransferWiseQuoteResponse;
import com.example.transferwise.model.quote.TransferwiseQuote;
import com.example.transferwise.model.recipient.*;
import com.example.transferwise.model.recipient.BankDetails.TransferwiseEURBankDetails;
import com.example.transferwise.model.recipient.BankDetails.TransferwiseGBPBankDetails;
import com.example.transferwise.model.recipient.BankDetails.TransferwiseUSDBankDetails;
import com.example.transferwise.model.transfer.TransferwiseFundTransferException;
import com.example.transferwise.model.transfer.TransferwiseTransfer;
import com.example.transferwise.model.transfer.TransferwiseTransferResponse;
import com.example.transferwise.model.transfer.TransferwiseTransferStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferwiseClientTest {

    @Mock
    private TransferwiseApi transferwiseApi;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransferwiseProfile transferwiseProfile;

    @Captor
    private ArgumentCaptor<TransferwiseRecipient<TransferwiseGBPBankDetails>> captorGBPRecipient;

    @Captor
    private ArgumentCaptor<TransferwiseRecipient<TransferwiseUSDBankDetails>> captorUSDRecipient;

    @Captor
    private ArgumentCaptor<TransferwiseRecipient<TransferwiseEURBankDetails>> captorEURRecipient;

    @Captor
    private ArgumentCaptor<TransferwiseQuote> captorQuote;

    @InjectMocks
    private TransferwiseClient transferwiseClient;

    private int transferId;

    @BeforeEach
    void setUp() {
        transferId =5;
    }

    @Test
    void checkGBPTransferCreated() throws TransferwiseAddRecipientException, SubmitQuoteException, TransferwiseCurrencyException, TransferwiseFundTransferException {
        TransferwisePaymentInstruction paymentInstruction = this.getGBPPaymentInstruction(4);
        TransferwiseTransferStatusResponse transferStatusResponse = new TransferwiseTransferStatusResponse();

        when(transferwiseApi.addRecipient(any(TransferwiseRecipient.class))).thenReturn(this.createRecipientResponse(1));
        when(transferwiseApi.submitQuote(any(TransferwiseQuote.class))).thenReturn(createQuoteResponse(2));
        when(transferwiseApi.submitTransfer(any(TransferwiseTransfer.class))).thenReturn(createTransferResponse(transferId));
        when(transferwiseApi.fundTransfer(transferId)).thenReturn(transferStatusResponse);
        when(transferwiseApi.checkTransferStatus(transferId)).thenReturn(transferStatusResponse);
        assertEquals(transferwiseClient.payInstruction(paymentInstruction), transferStatusResponse);

        verify(transferwiseApi, times(1)).addRecipient(captorGBPRecipient.capture());
        verify(transferwiseApi, times(1)).submitQuote(captorQuote.capture());
        verify(transferwiseApi, times(1)).submitTransfer(any(TransferwiseTransfer.class));
        verify(transferwiseApi, times(1)).fundTransfer(transferId);

        assertEquals(captorGBPRecipient.getValue().getCurrency(), paymentInstruction.getTargetCurrency());
        assertEquals(captorGBPRecipient.getValue().getAccountHolderName(), paymentInstruction.getAccountHolderName());
        assertEquals(captorGBPRecipient.getValue().getDetails().getSortCode(), paymentInstruction.getSortCode());
        assertEquals(captorGBPRecipient.getValue().getDetails().getAccountNumber(), paymentInstruction.getAccountNumber());
        assertEquals(captorQuote.getValue().getTargetAmount(), paymentInstruction.getAmount());
    }

    @Test
    void checkUSDTransferCreated() throws TransferwiseAddRecipientException, SubmitQuoteException, TransferwiseCurrencyException, TransferwiseFundTransferException {
        TransferwisePaymentInstruction paymentInstruction = this.getUSDPaymentInstruction(1);
        TransferwiseTransferStatusResponse transferStatusResponse = new TransferwiseTransferStatusResponse();

        when(transferwiseApi.addRecipient(any(TransferwiseRecipient.class))).thenReturn(this.createRecipientResponse(1));
        when(transferwiseApi.submitQuote(any(TransferwiseQuote.class))).thenReturn(createQuoteResponse(2));
        when(transferwiseApi.submitTransfer(any(TransferwiseTransfer.class))).thenReturn(createTransferResponse(transferId));
        when(transferwiseApi.fundTransfer(transferId)).thenReturn(transferStatusResponse);
        when(transferwiseApi.checkTransferStatus(transferId)).thenReturn(transferStatusResponse);
        assertEquals(transferwiseClient.payInstruction(paymentInstruction), transferStatusResponse);

        verify(transferwiseApi, times(1)).addRecipient(captorUSDRecipient.capture());
        verify(transferwiseApi, times(1)).submitQuote(captorQuote.capture());
        verify(transferwiseApi, times(1)).submitTransfer(any(TransferwiseTransfer.class));
        verify(transferwiseApi, times(1)).fundTransfer(transferId);

        assertEquals(captorUSDRecipient.getValue().getCurrency(), paymentInstruction.getTargetCurrency());
        assertEquals(captorUSDRecipient.getValue().getAccountHolderName(), paymentInstruction.getAccountHolderName());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAbartn(), paymentInstruction.getAbartn());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAccountNumber(), paymentInstruction.getAccountNumber());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAddress().getFirstLine(), paymentInstruction.getFirstLine());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAddress().getCity(), paymentInstruction.getCity());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAddress().getCountry(), paymentInstruction.getCountry());
        assertEquals(captorUSDRecipient.getValue().getDetails().getAddress().getPostCode(), paymentInstruction.getPostCode());
        assertEquals(captorQuote.getValue().getTargetAmount(), paymentInstruction.getAmount());
    }

    @Test
    void checkEURTransferCreated() throws TransferwiseAddRecipientException, SubmitQuoteException, TransferwiseCurrencyException, TransferwiseFundTransferException {
        TransferwisePaymentInstruction paymentInstruction = this.getEURPaymentInstruction(4);
        TransferwiseTransferStatusResponse transferStatusResponse = new TransferwiseTransferStatusResponse();

        when(transferwiseApi.addRecipient(any(TransferwiseRecipient.class))).thenReturn(this.createRecipientResponse(1));
        when(transferwiseApi.submitQuote(any(TransferwiseQuote.class))).thenReturn(createQuoteResponse(2));
        when(transferwiseApi.submitTransfer(any(TransferwiseTransfer.class))).thenReturn(createTransferResponse(transferId));
        when(transferwiseApi.fundTransfer(transferId)).thenReturn(transferStatusResponse);
        when(transferwiseApi.checkTransferStatus(transferId)).thenReturn(transferStatusResponse);
        assertEquals(transferwiseClient.payInstruction(paymentInstruction), transferStatusResponse);

        verify(transferwiseApi, times(1)).addRecipient(captorEURRecipient.capture());
        verify(transferwiseApi, times(1)).submitQuote(captorQuote.capture());
        verify(transferwiseApi, times(1)).submitTransfer(any(TransferwiseTransfer.class));
        verify(transferwiseApi, times(1)).fundTransfer(transferId);

        assertEquals(captorEURRecipient.getValue().getCurrency(), paymentInstruction.getTargetCurrency());
        assertEquals(captorEURRecipient.getValue().getAccountHolderName(), paymentInstruction.getAccountHolderName());
        assertEquals(captorEURRecipient.getValue().getDetails().getIBAN(), paymentInstruction.getIBAN());
        assertEquals(captorQuote.getValue().getTargetAmount(), paymentInstruction.getAmount());
    }



    @Test
    void checkUnknownCurrencyThrowsCurrencyError() {
        TransferwisePaymentInstruction paymentInstruction = new TransferwisePaymentInstruction(
                45
                , "CAN"
                , new BigDecimal("20.04")
                , "Bill Thomas"
                , "11-22-05"
                , "12345678"
        );
        assertThrows(TransferwiseCurrencyException.class, () -> {
            transferwiseClient.payInstruction(paymentInstruction);
        });
    }

    private TransferwisePaymentInstruction getGBPPaymentInstruction(int id){
        return new TransferwisePaymentInstruction(
                id
                , "GBP"
                , new BigDecimal("45.24")
                , "Bill Smith"
                , "40-30-20"
                , "12345678"
        );
    }

    private TransferwisePaymentInstruction getUSDPaymentInstruction(int id){
        return new TransferwisePaymentInstruction(
                id
                , "USD"
                , new BigDecimal("45.00")
                , "Mike Bobby"
                , null
                , "12345678"
                ,"GB"
                ,"London"
                ,"10025"
                ,"50 Branson Ave"
                ,null
                ,"111000025"
                ,null
        );
    }

    private TransferwisePaymentInstruction getEURPaymentInstruction(int id){
        return new TransferwisePaymentInstruction(
                id
                , "EUR"
                , new BigDecimal("45.64")
                , "Rob Taylor"
                , null
                , null
                ,null
                ,null
                ,null
                ,null
                ,"DE89370400440532013000"
                ,null
                ,null
        );
    }

    private TransferwiseRecipientResponse createRecipientResponse(int id){
        TransferwiseRecipientResponse recipientResponse = new TransferwiseRecipientResponse();
        recipientResponse.setId(id);
        return recipientResponse;
    }
    private TransferWiseQuoteResponse createQuoteResponse(int id){
        TransferWiseQuoteResponse quoteResponse = new TransferWiseQuoteResponse();
        quoteResponse.setId(id);
        return quoteResponse;
    }

    private TransferwiseTransferResponse createTransferResponse(int id){
        TransferwiseTransferResponse transferResponse = new TransferwiseTransferResponse();
        transferResponse.setId(id);
        return transferResponse;
    }
}
