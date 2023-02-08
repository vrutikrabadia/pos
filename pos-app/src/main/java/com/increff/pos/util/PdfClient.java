package com.increff.pos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.increff.pos.spring.Properties;

@Component
public class PdfClient {
    
    @Autowired
    private Properties properties;

    @Autowired
    private RestTemplate restTemplate;

    public <T> String getPdfBase64(T data, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> apiResponse = restTemplate.postForEntity(properties.getPdfAppUrl()+url, data, String.class);
        String responseBody = apiResponse.getBody();
        
        return responseBody;
    }
}
