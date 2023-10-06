package com.hepsiburada.hepsiburada.service;

import com.hepsiburada.hepsiburada.config.Credentials;
import com.hepsiburada.hepsiburada.config.Endpoints;
import com.hepsiburada.hepsiburada.utils.ServiceResponse;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;

public class BaseService {
    protected boolean isTestStage;
    protected Credentials credentials = new Credentials();
    protected Endpoints endpoints = new Endpoints();
    private String baseUrl;
    private ServiceResponse serviceResponse;

    public BaseService(boolean isTestStage, Credentials credentials, Endpoints endpoints) {
        this.isTestStage = isTestStage;
        this.credentials = credentials;
        this.endpoints = endpoints;
        this.baseUrl = isTestStage ? endpoints.getTestUrl() : endpoints.getProdUrl();
    }

    public String getUrl(String subdomain, String endpoint) {
        return "https://" + subdomain + this.baseUrl + endpoint;
    }

    public ServiceResponse request(String method, String endpoint, String dataOptions) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            String usernamePass = credentials.getUsername() + ":" + credentials.getPassword();
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(usernamePass.getBytes());
            headers.set("Authorization", basicAuth);
            headers.set("User-Agent", credentials.getUsername() + " - SelfIntegration");
            headers.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(endpoint);

            HttpEntity<String> entity = new HttpEntity<>(dataOptions, headers);
            ResponseEntity<String> response;

            HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setConnectTimeout(5000);

            if ("GET".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, String.class);
            } else if ("PUT".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, entity, String.class);
            } else {
                throw new IllegalArgumentException("Geçersiz HTTP isteği türü: " + method);
            }

            serviceResponse.setStatusCode(response.getStatusCode().toString());
            serviceResponse.setStatus(true);
            serviceResponse.setMessage("Success");
            serviceResponse.setData(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            serviceResponse.setStatusCode("500");
            serviceResponse.setStatus(false);
            serviceResponse.setMessage(e.getMessage());
            serviceResponse.setData("");
        }

        return serviceResponse;
    }

}
