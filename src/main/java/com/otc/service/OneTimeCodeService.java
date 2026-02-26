package com.otc.service;

import com.otc.config.OneTimeCodeCodeProperties;
import com.otc.config.OneTimeCodeExpiryProperties;
import com.otc.domain.OneTimeCodeDetail;
import com.otc.domain.OneTimeCodeState;
import com.otc.repo.OneTimeCodeDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OneTimeCodeService {

    private final OneTimeCodeDetailRepository repo;
    private final OneTimeCodeCodeGenerator generator;
    private final OneTimeCodeCodeProperties codeProps;
    private final OneTimeCodeExpiryProperties expiryProps;

    public OneTimeCodeService(OneTimeCodeDetailRepository repo,
                           OneTimeCodeCodeGenerator generator,
                           OneTimeCodeCodeProperties codeProps,
                           OneTimeCodeExpiryProperties expiryProps) {
        this.repo = repo;
        this.generator = generator;
        this.codeProps = codeProps;
        this.expiryProps = expiryProps;
    }

    @Transactional
    public OneTimeCodeDetail create(Long customerId, Long bankId, BigDecimal availableLoanLimit, Long expiryMinutes) {
        long minutes = (expiryMinutes != null && expiryMinutes > 0) ? expiryMinutes : expiryProps.defaultMinutes();
        Instant now = Instant.now();

        int attempts = 0;
        while (true) {
            attempts++;
            var code = generator.generate();

            OneTimeCodeDetail pass = new OneTimeCodeDetail();
            pass.setCustomerId(customerId);
            pass.setBankId(bankId);
            pass.setAvailableLoanLimit(availableLoanLimit);
            pass.setUsedAmount(BigDecimal.ZERO);
            pass.setCode(code.code());
            pass.setCodeFormatted(code.formatted());
            pass.setState(OneTimeCodeState.CREATED);
            pass.setValidFrom(now);
            pass.setValidTo(now.plus(minutes, ChronoUnit.MINUTES));
            pass.setActive(true);

            try {
                return repo.saveAndFlush(pass); // flush so unique constraint collision is caught here
            } catch (DataIntegrityViolationException ex) {
                if (attempts >= codeProps.maxRetries()) {
                    throw new OneTimeCodeException("Could not generate a unique One Time Code code after " + attempts + " attempts.");
                }
                // retry on collision
            }
        }
    }

    @Transactional
    public OneTimeCodeDetail activate(Long id) {
        OneTimeCodeDetail pass = getOrThrow(id);
        expireIfNeeded(pass);

        if (pass.getState() != OneTimeCodeState.CREATED) {
            throw new OneTimeCodeException("Only CREATED pass can be activated.");
        }
        pass.setState(OneTimeCodeState.ACTIVE);
        return repo.save(pass);
    }

    @Transactional
    public OneTimeCodeDetail useOnce(Long id, BigDecimal amount) {
        OneTimeCodeDetail pass = getOrThrow(id);
        expireIfNeeded(pass);

        if (pass.getState() != OneTimeCodeState.ACTIVE) {
            throw new OneTimeCodeException("Only ACTIVE pass can be used.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OneTimeCodeException("Amount must be positive.");
        }
        if (pass.getUsedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new OneTimeCodeException("This One Time Code is single-use and has already been used.");
        }
        if (amount.compareTo(pass.getAvailableLoanLimit()) > 0) {
            throw new OneTimeCodeException("Amount exceeds available limit.");
        }

        pass.setUsedAmount(amount);
        pass.setState(OneTimeCodeState.USED);
        pass.setActive(false);
        return repo.save(pass);
    }

    @Transactional
    public OneTimeCodeDetail cancel(Long id) {
        OneTimeCodeDetail pass = getOrThrow(id);

        if (pass.getState() == OneTimeCodeState.USED) {
            throw new OneTimeCodeException("USED pass cannot be cancelled.");
        }
        pass.setState(OneTimeCodeState.CANCELLED);
        pass.setActive(false);
        return repo.save(pass);
    }

    @Transactional
    public void expireIfNeeded(OneTimeCodeDetail pass) {
        if (pass.isExpired(Instant.now()) && (pass.getState() == OneTimeCodeState.CREATED || pass.getState() == OneTimeCodeState.ACTIVE)) {
            pass.setState(OneTimeCodeState.CANCELLED); // expiry mapped to CANCELLED (only states requested)
            pass.setActive(false);
            repo.save(pass);
        }
    }

    @Transactional
    public OneTimeCodeDetail get(Long id) {
        OneTimeCodeDetail pass = getOrThrow(id);
        expireIfNeeded(pass);
        return pass;
    }

    private OneTimeCodeDetail getOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new OneTimeCodeException("One Time Code not found: " + id));
    }
}
