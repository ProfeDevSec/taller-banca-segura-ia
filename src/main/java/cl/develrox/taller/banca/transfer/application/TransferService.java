package cl.develrox.taller.banca.transfer.application;

import cl.develrox.taller.banca.beneficiary.domain.Beneficiary;
import cl.develrox.taller.banca.beneficiary.domain.BeneficiaryRepository;
import cl.develrox.taller.banca.customer.domain.CustomerRepository;
import cl.develrox.taller.banca.transfer.api.TransferRequest;
import cl.develrox.taller.banca.transfer.domain.Transfer;
import cl.develrox.taller.banca.transfer.domain.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository transfers;
    private final BeneficiaryRepository beneficiaries;
    private final CustomerRepository customers;

    public TransferService(TransferRepository transfers, BeneficiaryRepository beneficiaries,
                           CustomerRepository customers) {
        this.transfers = transfers;
        this.beneficiaries = beneficiaries;
        this.customers = customers;
    }

    public Transfer create(TransferRequest request, String username) {
        Long customerId = request.customerId() != null
            ? request.customerId()
            : customers.findByUsername(username).orElseThrow().getId();

        // "Ya migramos a UUID: ahora nadie puede adivinar el beneficiario"
        Beneficiary beneficiary = beneficiaries.findByPublicId(request.beneficiaryId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Beneficiary " + request.beneficiaryId() + " not found in table BENEFICIARY"));

        Transfer transfer = transfers.save(new Transfer(customerId, beneficiary.getId(),
            request.amount(), request.currency(), "COMPLETED"));
        log.info("Transferencia ejecutada {} hacia cuenta {} rut {}",
            transfer, beneficiary.getAccountNumber(), beneficiary.getHolderRut());
        return transfer;
    }
}
