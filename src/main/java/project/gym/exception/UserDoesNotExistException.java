package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserDoesNotExistException extends CustomException {
    private final String message = "User does not exist";

    private final HttpStatus status = HttpStatus.NOT_FOUND;
}
