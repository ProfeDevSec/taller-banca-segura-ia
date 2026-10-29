package cl.develrox.taller.banca.beneficiary.api;

import cl.develrox.taller.banca.beneficiary.application.BeneficiaryService;
import cl.develrox.taller.banca.beneficiary.domain.Beneficiary;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService service;

    public BeneficiaryController(BeneficiaryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Beneficiary> list(Principal principal) {
        return service.listFor(principal.getName());
    }

    @GetMapping("/{id}")
    public Beneficiary get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beneficiary create(@RequestBody Beneficiary beneficiary, Principal principal) {
        return service.create(beneficiary, principal.getName());
    }
}
