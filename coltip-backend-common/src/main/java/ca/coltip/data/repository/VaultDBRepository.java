package ca.coltip.data.repository;

import ca.coltip.data.dto.VaultDBSecrets;
import org.springframework.data.repository.CrudRepository;

public interface VaultDBRepository extends CrudRepository<VaultDBSecrets,String> {
}
