package com.otc.config;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ecompass.expiry")
public record EcomPassExpiryProperties(
        @Min(1) long defaultMinutes
) {}
