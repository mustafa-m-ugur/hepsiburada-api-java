package com.hepsiburada.hepsiburada.service;

import com.hepsiburada.hepsiburada.config.Credentials;
import com.hepsiburada.hepsiburada.config.Endpoints;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class BaseService {
    protected boolean isTestStage;
    protected Credentials credentials = new Credentials();
    protected Endpoints endpoints = new Endpoints();
    private String baseUrl;

    public BaseService(boolean isTestStage, Credentials credentials, Endpoints endpoints, String baseUrl) {
        this.isTestStage = isTestStage;
        this.credentials = credentials;
        this.baseUrl = isTestStage ? this.endpoints.testUrl : this.endpoints.prodUrl;
    }

    public String getUrl(String subdomain, String endpoint) {
        return "https://" + subdomain + this.baseUrl + endpoint;
    }

    public Object request(String method, String endpoint, String dataOptions) {
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

            if ("GET".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, String.class);
            }  else if ("PUT".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, entity, String.class);
            } else {
                throw new IllegalArgumentException("Geçersiz HTTP isteği türü: " + method);
            }

            // TODO The ones from response.getBody will also be processed into a baseRequestResponse object.

            String responseBody = response.getBody();

            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
