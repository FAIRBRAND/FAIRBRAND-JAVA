package ca.coltip.data.repository;

import ca.coltip.data.dto.VaultCryptoSecrets;
import org.springframework.data.repository.CrudRepository;

public interface VaultCryptoRepository extends CrudRepository<VaultCryptoSecrets, String> {
}
