package project.gym.dto.form;

import lombok.Data;
import project.gym.model.Pass;
import project.gym.model.PaymentMethod;

@Data
public class SignUpFormData {
    private final Iterable<Pass> passes;
    private final Iterable<PaymentMethod> paymentMethods;
}
