package project.gym.dto.form;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Data;
import project.gym.model.Member;

@Data
public class EditProfileFormData {
    @JsonUnwrapped
    private final Member memberData;
}
