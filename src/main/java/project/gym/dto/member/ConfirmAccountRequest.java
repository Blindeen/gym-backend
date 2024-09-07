package project.gym.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ConfirmAccountRequest {
    @NotEmpty(message = "{validation.tokenIsRequired}")
    private String token;
}
