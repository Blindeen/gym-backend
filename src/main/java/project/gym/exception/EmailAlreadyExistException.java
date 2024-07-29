package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyExistException extends CustomException {
    public EmailAlreadyExistException() {
        resource = "email";
        messageKey = "exception.mail.exist";
        status = HttpStatus.CONFLICT;
        initialize();
    }
}
