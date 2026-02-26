package com.otc.mapper;

import com.otc.dto.OneTimeCodeResponse;
import com.otc.domain.OneTimeCodeDetail;

public class OneTimeCodeMapper {
    public static OneTimeCodeResponse toResponse(OneTimeCodeDetail p) {
        return new OneTimeCodeResponse(
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
