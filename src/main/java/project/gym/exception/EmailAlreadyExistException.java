package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyExistException extends CustomException {
    private final String resource = "email";
    private final String message = "Email already exists";
    private final HttpStatus status = HttpStatus.CONFLICT;
}
