package com.otc.service;

import com.otc.domain.OneTimeCodeState;
import com.otc.repo.OneTimeCodeDetailRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ExpirySweepScheduler {

    private final OneTimeCodeDetailRepository repo;
    private final OneTimeCodeService service;

    public ExpirySweepScheduler(OneTimeCodeDetailRepository repo, OneTimeCodeService service) {
        this.repo = repo;
        this.service = service;
    }

    @Scheduled(fixedDelayString = "${ecompass.scheduler.expiry-sweep-ms:60000}")
    public void sweep() {
        Instant now = Instant.now();
        var expiring = repo.findByStateInAndValidToBefore(List.of(OneTimeCodeState.CREATED, OneTimeCodeState.ACTIVE), now);
        for (var pass : expiring) {
            service.expireIfNeeded(pass);
        }
    }
}
