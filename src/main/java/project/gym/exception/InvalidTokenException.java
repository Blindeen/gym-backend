package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        resource = "token";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
