package com.otc.api;

import com.otc.api.dto.EcomPassResponse;
import com.otc.domain.EcomPassDetail;

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
