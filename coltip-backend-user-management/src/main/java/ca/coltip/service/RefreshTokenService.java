package ca.coltip.service;

import ca.coltip.data.dto.RefreshTokenDTO;
import ca.coltip.data.entities.User;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenDTO> findByToken(String token) throws Exception;
    RefreshTokenDTO createRefreshToken(User user);
    boolean isRefreshTokenExpired(RefreshTokenDTO refreshTokenDTO);
}
