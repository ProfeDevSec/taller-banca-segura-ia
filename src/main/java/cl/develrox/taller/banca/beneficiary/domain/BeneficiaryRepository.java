package cl.develrox.taller.banca.beneficiary.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByCustomerId(Long customerId);

    Optional<Beneficiary> findByPublicId(UUID publicId);

    Optional<Beneficiary> findByPublicIdAndCustomerId(UUID publicId, Long customerId);

    Optional<Beneficiary> findFirstByAliasOrderByIdDesc(String alias);
}
