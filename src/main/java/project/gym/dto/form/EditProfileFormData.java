package project.gym.dto.form;

import lombok.Data;
import project.gym.model.Member;
import project.gym.model.Pass;
import project.gym.model.PaymentMethod;

@Data
public class EditProfileFormData {
    private final Member userData;
    private final Iterable<Pass> passes;
    private final Iterable<PaymentMethod> paymentMethods;
}
