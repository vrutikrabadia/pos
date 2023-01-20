package com.increff.pos.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.increff.pos")
@EnableAsync
@EnableScheduling
@PropertySources({ //
		@PropertySource(value = "file:./pos.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {


}
