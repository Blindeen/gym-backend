package project.gym.exception.members.confirmation;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        resource = "token";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
