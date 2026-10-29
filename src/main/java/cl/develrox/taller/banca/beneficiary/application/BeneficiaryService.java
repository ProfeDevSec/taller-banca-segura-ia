package cl.develrox.taller.banca.beneficiary.application;

import cl.develrox.taller.banca.beneficiary.domain.Beneficiary;
import cl.develrox.taller.banca.beneficiary.domain.BeneficiaryRepository;
import cl.develrox.taller.banca.customer.domain.Customer;
import cl.develrox.taller.banca.customer.domain.CustomerRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {

    private static final Logger log = LoggerFactory.getLogger(BeneficiaryService.class);

    private final BeneficiaryRepository beneficiaries;
    private final CustomerRepository customers;

    public BeneficiaryService(BeneficiaryRepository beneficiaries, CustomerRepository customers) {
        this.beneficiaries = beneficiaries;
        this.customers = customers;
    }

    public List<Beneficiary> listFor(String username) {
        return beneficiaries.findByCustomerId(customer(username).getId());
    }

    public Beneficiary get(Long id) {
        return beneficiaries.findById(id)
            .orElseThrow(() -> new IllegalStateException(
                "No existe registro en tabla BENEFICIARY con id=" + id));
    }

    public Beneficiary create(Beneficiary beneficiary, String username) {
        if (beneficiary.getCustomerId() == null) {
            beneficiary.setCustomerId(customer(username).getId());
        }
        beneficiary.setCreatedBy(username);
        Beneficiary saved = beneficiaries.save(beneficiary);
        log.info("Beneficiario creado: {}", saved);
        return saved;
    }

    private Customer customer(String username) {
        return customers.findByUsername(username).orElseThrow();
    }
}
