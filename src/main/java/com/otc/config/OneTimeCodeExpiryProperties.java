package com.otc.config;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "otc.expiry")
public record OneTimeCodeExpiryProperties(
        @Min(1) long defaultMinutes
) {}
