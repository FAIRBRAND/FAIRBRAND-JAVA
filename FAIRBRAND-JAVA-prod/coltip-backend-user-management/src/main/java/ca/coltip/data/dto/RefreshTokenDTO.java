package ca.coltip.data.dto;

import ca.coltip.data.entities.RefreshToken;

import java.time.Instant;

public class RefreshTokenDTO {
    private int id;
    private UserDTO user;
    private Instant expiresAt;
    private String token;

    // Constructor that takes the entity as parameter and creates the DTO
    public RefreshTokenDTO(RefreshToken refreshToken) {
        this.id = refreshToken.getId();
        this.user = new UserDTO(refreshToken.getUser()); // Assuming a UserDTO exists
        this.expiresAt = refreshToken.getExpiresAt();
        this.token = refreshToken.getToken();
    }

    // Method to convert DTO back to entity
    public RefreshToken toEntity() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(this.id);
        refreshToken.setUser(this.user.toEntity()); // Assuming a toEntity method in UserDTO
        refreshToken.setExpiresAt(this.expiresAt);
        refreshToken.setToken(this.token);
        return refreshToken;
    }

    public int getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
