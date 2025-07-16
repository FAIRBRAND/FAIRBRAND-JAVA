package ca.coltip.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {
    @JsonProperty("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
