package project.gym.exception.auth;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyExistException extends CustomException {
    public EmailAlreadyExistException() {
        resource = "email";
        status = HttpStatus.CONFLICT;
        initialize();
    }
}
