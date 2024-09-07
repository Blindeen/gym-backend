package project.gym.dto.member;

import lombok.Data;

@Data
public class RegistrationResponse {
    private AuthenticationResponse authenticationResponse;
    private String confirmAccountToken;

    public RegistrationResponse(AuthenticationResponse authenticationResponse, String confirmAccountToken) {
        this.authenticationResponse = authenticationResponse;
        this.confirmAccountToken = confirmAccountToken;
    }
}
