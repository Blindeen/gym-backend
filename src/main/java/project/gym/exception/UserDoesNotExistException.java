package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserDoesNotExistException extends CustomException {
    public UserDoesNotExistException() {
        resource = "user";
        messageKey = "exception.user.not.exist";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
