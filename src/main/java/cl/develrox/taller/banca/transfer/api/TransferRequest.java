package cl.develrox.taller.banca.transfer.api;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * customerId se mantiene "por compatibilidad con la app movil antigua".
 */
public record TransferRequest(Long customerId, UUID beneficiaryId, BigDecimal amount, String currency) {
}
