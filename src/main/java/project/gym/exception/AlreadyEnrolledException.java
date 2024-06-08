package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyEnrolledException extends CustomException {
    private final String resource = "activity";
    private final String message = "You are already enrolled for this activity";
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
}
