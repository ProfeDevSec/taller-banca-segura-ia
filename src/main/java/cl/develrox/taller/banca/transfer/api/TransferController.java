package cl.develrox.taller.banca.transfer.api;

import cl.develrox.taller.banca.transfer.application.TransferService;
import cl.develrox.taller.banca.transfer.domain.Transfer;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer create(@RequestBody TransferRequest request, Principal principal) {
        return service.create(request, principal.getName());
    }
}
