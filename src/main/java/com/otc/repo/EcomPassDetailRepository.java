package com.otc.repo;

import com.otc.domain.EcomPassDetail;
import com.otc.domain.EcomPassState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EcomPassDetailRepository extends JpaRepository<EcomPassDetail, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeFormatted(String codeFormatted);

    List<EcomPassDetail> findByStateInAndValidToBefore(List<EcomPassState> states, Instant before);
}
