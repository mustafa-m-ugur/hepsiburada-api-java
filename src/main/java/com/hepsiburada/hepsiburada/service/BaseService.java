package com.hepsiburada.hepsiburada.service;

import com.hepsiburada.hepsiburada.config.Credentials;
import com.hepsiburada.hepsiburada.config.Endpoints;

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
    private static HttpURLConnection connection;

    public BaseService(boolean isTestStage, Credentials credentials, Endpoints endpoints, String baseUrl) {
        this.isTestStage = isTestStage;
        this.credentials = credentials;
        this.baseUrl = isTestStage ? this.endpoints.testUrl : this.endpoints.prodUrl;
    }

    public String getUrl(String subdomain, String endpoint) {
        return "https://" + subdomain + this.baseUrl + endpoint;
    }

    public Object request(String method, String endpoint, String dataOptions) {
        BufferedReader reader;
        StringBuffer response = new StringBuffer();
        String responseData;

        try {

            String usernamePass = credentials.getUsername() + ":" + credentials.getPassword();
            String basicAuth = "Basic " + Base64.getEncoder().encode(usernamePass.getBytes());

            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("User-Agent", credentials.getUsername() + " - SelfIntegration");
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);

            if (method.equals("POST") && dataOptions != null) {
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(dataOptions.getBytes());
                }
            }

            if (connection.getResponseCode() < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((responseData = reader.readLine()) != null) {
                    response.append(responseData);
                }
                reader.close();

            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((responseData = reader.readLine()) != null) {
                    response.append(responseData);
                }
                reader.close();
            }

        } catch (Exception e) {

        }

        //baseResponseModel will be created and it will be returned as return
        return "";
    }

}
