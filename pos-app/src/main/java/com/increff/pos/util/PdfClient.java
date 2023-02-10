package com.increff.pos.util;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.increff.pos.spring.Properties;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class PdfClient {


    private HashMap<String, String> urlMap = new HashMap<>();
    @Autowired
    private Properties properties;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        for (int i = 0; i < properties.getPdfEndpointNames().size(); i++) {
            urlMap.put(properties.getPdfEndpointNames().get(i), properties.getPdfEndpointUrls().get(i));
        }
    }

    public <T> String getPdfBase64(T data, String endpointName) throws RestClientException, ApiException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> apiResponse = null;
        if(urlMap.getOrDefault(endpointName,null) == null)
            throw new ApiException("Invalid endpoint name: " + endpointName);
        try {
            apiResponse = restTemplate.postForEntity(properties.getPdfAppUrl() + urlMap.get(endpointName), data, String.class);
        } catch (RestClientException ex) {
            throw ex;
        }

        String responseBody = apiResponse.getBody();
        
        return responseBody;
    }
}
