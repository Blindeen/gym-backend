package project.gym.dto.member;

import lombok.Data;

@Data
public class RegistrationResponse {
    private final AuthenticationResponse authenticationResponse;
    private final String confirmAccountToken;
}
