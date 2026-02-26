package com.otc.service;

import com.otc.domain.EcomPassState;
import com.otc.repo.EcomPassDetailRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ExpirySweepScheduler {

    private final EcomPassDetailRepository repo;
    private final EcomPassService service;

    public ExpirySweepScheduler(EcomPassDetailRepository repo, EcomPassService service) {
        this.repo = repo;
        this.service = service;
    }

    @Scheduled(fixedDelayString = "${ecompass.scheduler.expiry-sweep-ms:60000}")
    public void sweep() {
        Instant now = Instant.now();
        var expiring = repo.findByStateInAndValidToBefore(List.of(EcomPassState.CREATED, EcomPassState.ACTIVE), now);
        for (var pass : expiring) {
            service.expireIfNeeded(pass);
        }
    }
}
