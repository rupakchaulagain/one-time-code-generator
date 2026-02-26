package com.otc.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "otc.code")
public record OneTimeCodeCodeProperties(
        @NotBlank String prefix,
        @NotBlank String separator,
        @NotEmpty List<@Min(1) Integer> groups,
        @NotBlank @Size(min = 8) String alphabet,
        @Min(1) int maxRetries
) {}
