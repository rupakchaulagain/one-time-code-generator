package com.foneloan.ecompass.api.dto;

import com.foneloan.ecompass.domain.EcomPassState;

import java.math.BigDecimal;
import java.time.Instant;

public record EcomPassResponse(
        Long id,
        String codeFormatted,
        BigDecimal availableLoanLimit,
        BigDecimal usedAmount,
        EcomPassState state,
        Instant validFrom,
        Instant validTo,
        boolean active
) {}
