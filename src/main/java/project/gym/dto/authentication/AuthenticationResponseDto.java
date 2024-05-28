package project.gym.dto.authentication;

import lombok.Data;

@Data
public class AuthenticationResponseDto {
    private String token;

    public AuthenticationResponseDto(String token) {
        this.token = token;
    }
}
