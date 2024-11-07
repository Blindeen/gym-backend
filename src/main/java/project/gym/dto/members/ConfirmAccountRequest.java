package project.gym.dto.members;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ConfirmAccountRequest {
    @NotEmpty(message = "{validation.tokenIsRequired}")
    private String token;
}
