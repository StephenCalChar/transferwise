package com.example.transferwise;

import com.example.transferwise.model.*;
import com.example.transferwise.model.balance.TransferwiseCheckBalanceResponse;
import com.example.transferwise.model.quote.SubmitQuoteException;
import com.example.transferwise.model.quote.TransferWiseQuoteResponse;
import com.example.transferwise.model.quote.TransferwiseQuote;
import com.example.transferwise.model.recipient.*;
import com.example.transferwise.model.transfer.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    TransferWiseQuoteResponse submitQuote(TransferwiseQuote transferwiseQuote) throws SubmitQuoteException {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseQuote> entity = new HttpEntity<>(transferwiseQuote, headers);
        try {
            ResponseEntity<TransferWiseQuoteResponse> responseEntity = this.restOps.exchange(
                    BASE_URL + QUOTES_ENDPOINT
                    , HttpMethod.POST
                    , entity
                    , TransferWiseQuoteResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new SubmitQuoteException("Could not create quote" + e.getCause());
        }


    }

    TransferwiseRecipientResponse addRecipient(TransferwiseRecipient transferwiseRecipient) throws TransferwiseAddRecipientException {
        try {
            HttpHeaders headers = this.authToken.postHttpHeaders();
            HttpEntity<TransferwiseRecipient> entity = new HttpEntity<>(transferwiseRecipient, headers);
            ResponseEntity<TransferwiseRecipientResponse> responseEntity = this.restOps.exchange(
                    BASE_URL + ACCOUNTS_ENDPOINT
                    , HttpMethod.POST
                    , entity
                    , new ParameterizedTypeReference<TransferwiseRecipientResponse>() {
                    }
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new TransferwiseAddRecipientException("Error Creating Recipient" + e.getCause());
        }
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

    //handle insufficient funds
    TransferwiseTransferStatusResponse fundTransfer(int transferId) throws TransferwiseFundTransferException {
        HttpHeaders headers = this.authToken.postHttpHeaders();
        HttpEntity<TransferwiseFundTransfer> entity = new HttpEntity<>(new TransferwiseFundTransfer(), headers);
        String transferIdUrl = "/" + transferId + "/";
        try {
            ResponseEntity<TransferwiseTransferStatusResponse> responseEntity = this.restOps.exchange(
                    BASE_URL + TRANSFERS_ENDPOINT + transferIdUrl + PAYMENTS_ENDPOINT
                    , HttpMethod.POST
                    , entity
                    , TransferwiseTransferStatusResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getClass());
            throw new TransferwiseFundTransferException("Unable to Fund Transfer" + e.getCause());
        }

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

    List<TransferwiseTransferResponse> getTransfersByStatus( int profileId, String... args) {
        String statusQueryString = String.join(",", args);
        HttpEntity entity = new HttpEntity<>(this.authToken.getHttpHeaders());
        ResponseEntity<List<TransferwiseTransferResponse>> responseEntity = this.restOps.exchange(
                BASE_URL + TRANSFERS_ENDPOINT + "/?profile=" + profileId + "&status=" + statusQueryString
                , HttpMethod.GET
                , entity
                , new ParameterizedTypeReference<List<TransferwiseTransferResponse>>() {
                }
        );
        return responseEntity.getBody();
    }
}
