package project.gym.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "{validation.emailIsRequired}")
    private String email;

    @NotEmpty(message = "{validation.passwordIsRequired}")
    private String password;
}
