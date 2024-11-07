package project.gym.dto.members.password;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotEmpty(message = "{validation.emailIsRequired}")
    private String email;
}
