package com.increff.pos.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

import java.util.List;

@Component
@Getter
public class Properties {
    @Value("${app.baseUrl}")
	private String baseUrl;

    @Value("${cache.location}")
    private String cacheLocation;

    @Value("${supervisor.email}")
    private String supervisorEmail;

    @Value("${app.name}")
	private String name;

	@Value("${app.version}")
	private String version;

    @Value("${pdfApp.url}")
    private String pdfAppUrl;

    @Value("#{'${pdfApp.endpointNames}'.split(',')}")
    private List<String> pdfEndpointNames;

    @Value("#{'${pdfApp.endpointUrls}'.split(',')}")
    private List<String> pdfEndpointUrls;


}
