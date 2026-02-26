package com.foneloan.ecompass.api;

import com.foneloan.ecompass.api.dto.EcomPassResponse;
import com.foneloan.ecompass.domain.EcomPassDetail;

public class EcomPassMapper {
    public static EcomPassResponse toResponse(EcomPassDetail p) {
        return new EcomPassResponse(
                p.getId(),
                p.getCodeFormatted(),
                p.getAvailableLoanLimit(),
                p.getUsedAmount(),
                p.getState(),
                p.getValidFrom(),
                p.getValidTo(),
                p.isActive()
        );
    }
}
