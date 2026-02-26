package com.otc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ecom_pass_detail",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ecom_pass_code", columnNames = {"code"}),
                @UniqueConstraint(name = "uk_ecom_pass_code_formatted", columnNames = {"code_formatted"})
        },
        indexes = {
                @Index(name = "idx_ecom_pass_customer", columnList = "customer_id"),
                @Index(name = "idx_ecom_pass_state", columnList = "state"),
                @Index(name = "idx_ecom_pass_valid_to", columnList = "valid_to")
        }
)
public class EcomPassDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 64)
    private String code;

    @Column(name = "code_formatted", nullable = false, length = 128)
    private String codeFormatted;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "bank_id", nullable = false)
    private Long bankId;

    @Column(name = "available_loan_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal availableLoanLimit = BigDecimal.ZERO;

    @Column(name = "used_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal usedAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 16)
    private EcomPassState state = EcomPassState.CREATED;

    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "valid_to", nullable = false)
    private Instant validTo;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public boolean isExpired(Instant now) {
        return now.isAfter(validTo);
    }
}
