package com.increff.pos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PdfClient {
    @Value("${cache.location}")
    private String cacheLocation;

    @Value("${pdfapp.url}")
    private String pdfAppUrl;

    @Autowired
    private RestTemplate restTemplate;

    public <T> String getPdfBase64(T data, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> apiResponse = restTemplate.postForEntity(pdfAppUrl+url, data, String.class);
        String responseBody = apiResponse.getBody();
        
        return responseBody;
    }
}
