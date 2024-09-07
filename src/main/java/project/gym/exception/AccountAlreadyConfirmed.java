package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountAlreadyConfirmed extends CustomException {
    public AccountAlreadyConfirmed() {
        resource = "account";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
