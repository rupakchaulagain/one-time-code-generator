package com.otc.repo;

import com.otc.domain.OneTimeCodeDetail;
import com.otc.domain.OneTimeCodeState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface OneTimeCodeDetailRepository extends JpaRepository<OneTimeCodeDetail, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeFormatted(String codeFormatted);

    List<OneTimeCodeDetail> findByStateInAndValidToBefore(List<OneTimeCodeState> states, Instant before);
}
