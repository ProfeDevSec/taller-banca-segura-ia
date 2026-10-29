package cl.develrox.taller.banca.transfer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    private Long customerId;
    private Long beneficiaryId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Instant createdAt;

    protected Transfer() {
    }

    public Transfer(Long customerId, Long beneficiaryId, BigDecimal amount, String currency, String status) {
        this.customerId = customerId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    @PrePersist
    void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public Long getCustomerId() { return customerId; }
    public Long getBeneficiaryId() { return beneficiaryId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Transfer{id=" + id + ", customerId=" + customerId + ", beneficiaryId=" + beneficiaryId
            + ", amount=" + amount + ", currency='" + currency + "', status='" + status + "'}";
    }
}
