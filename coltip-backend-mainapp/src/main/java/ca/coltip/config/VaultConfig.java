package ca.coltip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.net.URI;

@Configuration
public class VaultConfig {

    @Bean
    public VaultTemplate vaultTemplate(@Value("${vault.uri}") String uri,
                                       @Value("${vault.token}") String token) {
        VaultEndpoint endpoint = VaultEndpoint.from(URI.create(uri)); // adjust host/port
        ClientAuthentication clientAuthentication = new TokenAuthentication(token);
        return new VaultTemplate(endpoint, clientAuthentication);
    }
}
