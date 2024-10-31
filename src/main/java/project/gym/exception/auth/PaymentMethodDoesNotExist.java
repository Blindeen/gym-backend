package project.gym.exception.auth;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class PaymentMethodDoesNotExist extends CustomException {
    public PaymentMethodDoesNotExist() {
        resource = "payment method";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
