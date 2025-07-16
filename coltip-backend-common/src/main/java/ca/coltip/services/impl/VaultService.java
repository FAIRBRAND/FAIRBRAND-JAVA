package ca.coltip.services.impl;

import ca.coltip.data.dto.VaultCryptoSecrets;
import ca.coltip.data.dto.VaultDBSecrets;
import ca.coltip.data.repository.VaultCryptoRepository;
import ca.coltip.data.repository.VaultDBRepository;
import ca.coltip.services.IVaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaultService implements IVaultService {

    private VaultDBSecrets vaultDBSecrets;
    private VaultCryptoSecrets vaultCryptoSecrets;

    @Autowired
    private VaultDBRepository vaultDBRepository;

    @Autowired
    private VaultCryptoRepository vaultCryptoRepository;

    public void getValues(){
        vaultDBSecrets = vaultDBRepository.findAllById(List.of("coltip/database")).iterator().next();
        vaultCryptoSecrets = vaultCryptoRepository.findAllById(List.of("coltip/encryption")).iterator().next();
    }

    @Override
    public String getDBGeneralPass(){
        return vaultDBSecrets.getGenpass();
    }
    @Override
    public String getDbFlywayPass(){
        return  vaultDBSecrets.getFlywaypass();
    }
    @Override
    public String getEncKey(){
        return vaultCryptoSecrets.getEncKey();
    }
}
