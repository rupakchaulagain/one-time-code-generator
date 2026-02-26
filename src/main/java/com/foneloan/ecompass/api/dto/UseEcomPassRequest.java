package com.foneloan.ecompass.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UseEcomPassRequest(
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount
) {}
