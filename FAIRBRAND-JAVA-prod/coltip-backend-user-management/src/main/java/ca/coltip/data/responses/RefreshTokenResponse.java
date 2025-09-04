package ca.coltip.data.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponse {
    @JsonProperty("refreshToken")
    private String refreshToken;

    public RefreshTokenResponse(String token) {
        refreshToken = token;
    }
}
