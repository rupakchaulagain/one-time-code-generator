package com.foneloan.ecompass.repo;

import com.foneloan.ecompass.domain.EcomPassDetail;
import com.foneloan.ecompass.domain.EcomPassState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EcomPassDetailRepository extends JpaRepository<EcomPassDetail, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeFormatted(String codeFormatted);

    List<EcomPassDetail> findByStateInAndValidToBefore(List<EcomPassState> states, Instant before);
}
