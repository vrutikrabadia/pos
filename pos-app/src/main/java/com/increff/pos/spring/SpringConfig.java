package com.increff.pos.spring;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.increff.pos","com.increff.pdf"})
@EnableAsync
@EnableScheduling
@PropertySources({ //
		@PropertySource(value = "file:./src/main/resources/com/increff/pos/pos.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:./src/main/resources/com/increff/pos/pdfClient.properties", ignoreResourceNotFound = true)//
})
public class SpringConfig {

	@Value("${server.timezone}")
	private String serverTimezone;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(serverTimezone));
	}


}
