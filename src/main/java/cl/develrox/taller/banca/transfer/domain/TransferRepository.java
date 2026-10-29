package cl.develrox.taller.banca.transfer.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findByCustomerId(Long customerId);
}
