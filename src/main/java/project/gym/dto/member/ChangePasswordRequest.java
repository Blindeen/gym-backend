package project.gym.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotEmpty(message = "{validation.tokenIsRequired}")
    private String token;

    @NotEmpty(message = "{validation.passwordIsRequired}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.invalidPasswordPattern}")
    private String password;
}
