package com.example.transferwise;


import com.example.transferwise.model.TransferwisePaymentInstruction;
import com.example.transferwise.model.TransferwiseProfile;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferwiseClientTest {

    @Mock
    private TransferwiseApi transferwiseApi;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransferwiseProfile transferwiseProfile;

    @InjectMocks
    private TransferwiseClient transferwiseClient;

    @BeforeEach
    void setUp(){
    }

    @Test
    void checkGBPRecipientCreated() {
        transferwiseClient = new TransferwiseClient(restTemplate);
        TransferwisePaymentInstruction paymentInstruction = new TransferwisePaymentInstruction(
                45
                , "GBP"
                , new BigDecimal(45.24)
                , "Bill Smith"
                , "40-30-20"
                , "12345678"
        );
        //transferwiseClient.payInstruction(paymentInstruction);
    }
}
