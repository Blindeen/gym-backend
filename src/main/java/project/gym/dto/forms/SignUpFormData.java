package project.gym.dto.forms;

import lombok.Data;
import project.gym.model.PassType;
import project.gym.model.PaymentMethod;

@Data
public class SignUpFormData {
    private final Iterable<PassType> passes;
    private final Iterable<PaymentMethod> paymentMethods;
}
