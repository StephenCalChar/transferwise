package com.example.transferwise;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AuthToken {
    // full access sandbox token, do not push to remote.
    private final static String AUTH_TOKEN = "";
    private final static String HEADER_PREFIX = "Bearer ";

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", HEADER_PREFIX + AUTH_TOKEN);
        return headers;
    }

    public HttpHeaders postHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", HEADER_PREFIX + AUTH_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
