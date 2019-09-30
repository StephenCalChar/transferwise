package com.example.transferwise;

import com.example.transferwise.model.*;
import com.example.transferwise.model.balance.TransferwiseCheckBalanceResponse;
import com.example.transferwise.model.quote.TransferWiseQuoteResponse;
import com.example.transferwise.model.quote.TransferwiseQuote;
import com.example.transferwise.model.recipient.TransferwiseGBPBankDetails;
import com.example.transferwise.model.recipient.TransferwiseRecipient;
import com.example.transferwise.model.recipient.TransferwiseRecipientResponse;
import com.example.transferwise.model.transfer.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferwiseApi {
    private static final String BASE_URL = "https://api.sandbox.transferwise.tech/v1/";
    private static final String PROFILE_ENDPOINT = "profiles";
    private static final String QUOTES_ENDPOINT = "quotes";
    private static final String ACCOUNTS_ENDPOINT = "accounts";
    private static final String TRANSFERS_ENDPOINT = "transfers";
    private static final String PAYMENTS_ENDPOINT = "payments";
    private static final String ACCOUNT_BALANCE_ENDPOINT = "borderless-accounts?profileId=";

    private RestOperations restOps;
    private AuthToken authToken;

    public TransferwiseApi(RestOperations restOperations) {
        this.restOps = restOperations;
        this.authToken = new AuthToken();
    }

    // All these http requests need to be in try catch blocks handling errors correctly.
    TransferwiseProfile getTransferwiseProfile() {
        HttpEntity entity = new HttpEntity<>(this.authToken.getHttpHeaders());
        ResponseEntity<List<TransferwiseProfile>> responseEntity = this.restOps.exchange(
                BASE_URL + PROFILE_ENDPOINT
                , HttpMethod.GET
                , entity
                , new ParameterizedTypeReference<List<TransferwiseProfile>>() {
                });
        //this gets business profile. Maybe Stream instead of index?
        //responseEntity.getBody().stream().filter(profile->profile.getType().equals("business")).findFirst().get();
        return Objects.requireNonNull(responseEntity.getBody()).get(1);
    }

    TransferWiseQuoteResponse submitQuote(TransferwiseQuote transferwiseQuote) {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseQuote> entity = new HttpEntity<>(transferwiseQuote, headers);
        ResponseEntity<TransferWiseQuoteResponse> responseEntity = this.restOps.exchange(
                BASE_URL + QUOTES_ENDPOINT
                , HttpMethod.POST
                , entity
                , TransferWiseQuoteResponse.class
        );
        return responseEntity.getBody();

    }

    //Catch invalid bank details error.
    //Probably want this to take a type as a parameter.
    TransferwiseRecipientResponse addRecipient(TransferwiseRecipient transferwiseRecipient)  {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseRecipient> entity = new HttpEntity<>(transferwiseRecipient, headers);
            ResponseEntity<TransferwiseRecipientResponse<TransferwiseGBPBankDetails>> responseEntity = this.restOps.exchange(
                    BASE_URL + ACCOUNTS_ENDPOINT
                    , HttpMethod.POST
                    , entity
                    , new ParameterizedTypeReference<TransferwiseRecipientResponse<TransferwiseGBPBankDetails>>() {
                    }
            );
            return responseEntity.getBody();

    }

    TransferwiseTransferResponse submitTransfer(TransferwiseTransfer transferwiseTransfer) {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseTransfer> entity = new HttpEntity<>(transferwiseTransfer, headers);
        ResponseEntity<TransferwiseTransferResponse> responseEntity = this.restOps.exchange(
                BASE_URL + TRANSFERS_ENDPOINT
                , HttpMethod.POST
                , entity
                , TransferwiseTransferResponse.class
        );
        return responseEntity.getBody();
    }

    TransferwiseTransferStatusResponse fundTransfer(int transferId) {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseFundTransfer> entity = new HttpEntity<>(new TransferwiseFundTransfer(), headers);
        String transferIdUrl = "/" + transferId + "/";
        ResponseEntity<TransferwiseTransferStatusResponse> responseEntity = this.restOps.exchange(
                BASE_URL + TRANSFERS_ENDPOINT + transferIdUrl + PAYMENTS_ENDPOINT
                , HttpMethod.POST
                , entity
                , TransferwiseTransferStatusResponse.class
        );
        return responseEntity.getBody();
    }

    TransferwiseTransferStatusResponse checkTransferStatus(int transferId) {
        HttpHeaders headers = this.authToken.getHttpHeaders();
        HttpEntity entity = new HttpEntity<>(this.authToken.getHttpHeaders());
        ResponseEntity<TransferwiseTransferStatusResponse> responseEntity = this.restOps.exchange(
                BASE_URL + TRANSFERS_ENDPOINT + "/" + transferId
                , HttpMethod.GET
                , entity
                , TransferwiseTransferStatusResponse.class
        );
        return responseEntity.getBody();
    }

    TransferwiseCheckBalanceResponse checkAccountBalance(int profileId) {
        HttpEntity entity = new HttpEntity<>(this.authToken.getHttpHeaders());
        ResponseEntity<List<TransferwiseCheckBalanceResponse>> responseEntity = this.restOps.exchange(
                BASE_URL + ACCOUNT_BALANCE_ENDPOINT + profileId
                , HttpMethod.GET
                , entity
                , new ParameterizedTypeReference<List<TransferwiseCheckBalanceResponse>>() {
                }
        );
        return Objects.requireNonNull(responseEntity.getBody()).get(0);
    }

}
