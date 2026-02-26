package com.otc.dto;

import com.otc.domain.OneTimeCodeState;

import java.math.BigDecimal;
import java.time.Instant;

public record OneTimeCodeResponse(
        Long id,
        String codeFormatted,
        BigDecimal availableLoanLimit,
        BigDecimal usedAmount,
        OneTimeCodeState state,
        Instant validFrom,
        Instant validTo,
        boolean active
) {}
