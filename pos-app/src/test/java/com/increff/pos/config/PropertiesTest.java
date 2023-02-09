package com.increff.pos.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesTest {
    @Value("${supervisor.email}")
    private String supervisorEmail;
}
