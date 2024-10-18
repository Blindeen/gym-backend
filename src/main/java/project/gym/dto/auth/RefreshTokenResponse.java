package project.gym.dto.auth;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private final String accessToken;
    private final String refreshToken;
}
