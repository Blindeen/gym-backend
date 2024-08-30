package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentMethodDoesNotExist extends CustomException {
    public PaymentMethodDoesNotExist() {
        resource = "payment method";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
