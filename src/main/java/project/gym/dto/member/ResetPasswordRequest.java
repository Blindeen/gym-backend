package project.gym.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotEmpty(message = "{validation.emailIsRequired}")
    private String email;
}
