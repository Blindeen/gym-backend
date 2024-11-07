package project.gym.exception.members.confirmation;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class AccountAlreadyConfirmed extends CustomException {
    public AccountAlreadyConfirmed() {
        resource = "account";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
