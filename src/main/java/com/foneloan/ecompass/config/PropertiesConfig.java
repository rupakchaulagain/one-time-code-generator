package com.foneloan.ecompass.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EcomPassCodeProperties.class, EcomPassExpiryProperties.class, SchedulerProperties.class})
public class PropertiesConfig {
}
