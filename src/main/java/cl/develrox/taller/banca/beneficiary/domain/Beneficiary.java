package cl.develrox.taller.banca.beneficiary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    private Long customerId;
    private String alias;
    private String holderName;
    private String holderRut;
    private String bankCode;
    private String accountNumber;
    private Integer internalRiskScore;

    @Enumerated(EnumType.STRING)
    private BeneficiaryStatus status;

    private String createdBy;
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
        if (status == null) {
            status = BeneficiaryStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UUID getPublicId() { return publicId; }
    public void setPublicId(UUID publicId) { this.publicId = publicId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getHolderRut() { return holderRut; }
    public void setHolderRut(String holderRut) { this.holderRut = holderRut; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public Integer getInternalRiskScore() { return internalRiskScore; }
    public void setInternalRiskScore(Integer internalRiskScore) { this.internalRiskScore = internalRiskScore; }
    public BeneficiaryStatus getStatus() { return status; }
    public void setStatus(BeneficiaryStatus status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Beneficiary{id=" + id + ", publicId=" + publicId + ", customerId=" + customerId
            + ", alias='" + alias + "', holderName='" + holderName + "', holderRut='" + holderRut
            + "', bankCode='" + bankCode + "', accountNumber='" + accountNumber
            + "', internalRiskScore=" + internalRiskScore + ", status=" + status + "}";
    }
}
