package com.otc.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEcomPassRequest(
        @NotNull Long customerId,
        @NotNull Long bankId,
        @NotNull BigDecimal availableLoanLimit,
        @Min(1) Long expiryMinutes
) {}
