package com.otc.service;

import com.otc.config.EcomPassCodeProperties;
import com.otc.config.EcomPassExpiryProperties;
import com.otc.domain.EcomPassDetail;
import com.otc.domain.EcomPassState;
import com.otc.repo.EcomPassDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class EcomPassService {

    private final EcomPassDetailRepository repo;
    private final EcomPassCodeGenerator generator;
    private final EcomPassCodeProperties codeProps;
    private final EcomPassExpiryProperties expiryProps;

    public EcomPassService(EcomPassDetailRepository repo,
                           EcomPassCodeGenerator generator,
                           EcomPassCodeProperties codeProps,
                           EcomPassExpiryProperties expiryProps) {
        this.repo = repo;
        this.generator = generator;
        this.codeProps = codeProps;
        this.expiryProps = expiryProps;
    }

    @Transactional
    public EcomPassDetail create(Long customerId, Long bankId, BigDecimal availableLoanLimit, Long expiryMinutes) {
        long minutes = (expiryMinutes != null && expiryMinutes > 0) ? expiryMinutes : expiryProps.defaultMinutes();
        Instant now = Instant.now();

        int attempts = 0;
        while (true) {
            attempts++;
            var code = generator.generate();

            EcomPassDetail pass = new EcomPassDetail();
            pass.setCustomerId(customerId);
            pass.setBankId(bankId);
            pass.setAvailableLoanLimit(availableLoanLimit);
            pass.setUsedAmount(BigDecimal.ZERO);
            pass.setCode(code.code());
            pass.setCodeFormatted(code.formatted());
            pass.setState(EcomPassState.CREATED);
            pass.setValidFrom(now);
            pass.setValidTo(now.plus(minutes, ChronoUnit.MINUTES));
            pass.setActive(true);

            try {
                return repo.saveAndFlush(pass); // flush so unique constraint collision is caught here
            } catch (DataIntegrityViolationException ex) {
                if (attempts >= codeProps.maxRetries()) {
                    throw new EcomPassException("Could not generate a unique Ecom Pass code after " + attempts + " attempts.");
                }
                // retry on collision
            }
        }
    }

    @Transactional
    public EcomPassDetail activate(Long id) {
        EcomPassDetail pass = getOrThrow(id);
        expireIfNeeded(pass);

        if (pass.getState() != EcomPassState.CREATED) {
            throw new EcomPassException("Only CREATED pass can be activated.");
        }
        pass.setState(EcomPassState.ACTIVE);
        return repo.save(pass);
    }

    @Transactional
    public EcomPassDetail useOnce(Long id, BigDecimal amount) {
        EcomPassDetail pass = getOrThrow(id);
        expireIfNeeded(pass);

        if (pass.getState() != EcomPassState.ACTIVE) {
            throw new EcomPassException("Only ACTIVE pass can be used.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new EcomPassException("Amount must be positive.");
        }
        if (pass.getUsedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new EcomPassException("This Ecom Pass is single-use and has already been used.");
        }
        if (amount.compareTo(pass.getAvailableLoanLimit()) > 0) {
            throw new EcomPassException("Amount exceeds available limit.");
        }

        pass.setUsedAmount(amount);
        pass.setState(EcomPassState.USED);
        pass.setActive(false);
        return repo.save(pass);
    }

    @Transactional
    public EcomPassDetail cancel(Long id) {
        EcomPassDetail pass = getOrThrow(id);

        if (pass.getState() == EcomPassState.USED) {
            throw new EcomPassException("USED pass cannot be cancelled.");
        }
        pass.setState(EcomPassState.CANCELLED);
        pass.setActive(false);
        return repo.save(pass);
    }

    @Transactional
    public void expireIfNeeded(EcomPassDetail pass) {
        if (pass.isExpired(Instant.now()) && (pass.getState() == EcomPassState.CREATED || pass.getState() == EcomPassState.ACTIVE)) {
            pass.setState(EcomPassState.CANCELLED); // expiry mapped to CANCELLED (only states requested)
            pass.setActive(false);
            repo.save(pass);
        }
    }

    @Transactional
    public EcomPassDetail get(Long id) {
        EcomPassDetail pass = getOrThrow(id);
        expireIfNeeded(pass);
        return pass;
    }

    private EcomPassDetail getOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new EcomPassException("Ecom Pass not found: " + id));
    }
}
