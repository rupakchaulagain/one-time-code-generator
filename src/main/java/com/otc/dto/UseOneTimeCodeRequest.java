package com.otc.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UseOneTimeCodeRequest(
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount
) {}
