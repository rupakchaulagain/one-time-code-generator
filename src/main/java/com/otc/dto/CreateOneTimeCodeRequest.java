package com.otc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOneTimeCodeRequest(
        @NotNull Long customerId,
        @NotNull Long bankId,
        @NotNull BigDecimal availableLoanLimit,
        @Min(1) Long expiryMinutes
) {}
