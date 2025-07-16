package ca.coltip.data.dto;

import org.springframework.vault.repository.mapping.Secret;

@Secret
public class VaultCryptoSecrets {
    private String encKey;

    public VaultCryptoSecrets(String encKey) {
        this.encKey = encKey;
    }

    public String getEncKey() {
        return encKey;
    }
}
