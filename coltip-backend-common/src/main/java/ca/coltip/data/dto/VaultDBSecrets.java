package ca.coltip.data.dto;

import org.springframework.vault.repository.mapping.Secret;

@Secret
public class VaultDBSecrets {
    private String genpass;
    private String flywaypass;

    public VaultDBSecrets(String genpass, String dbFlywayPass) {
        this.genpass = genpass;
        this.flywaypass = dbFlywayPass;
    }

    public String getGenpass() {
        return genpass;
    }

    public String getFlywaypass() {
        return flywaypass;
    }
}
