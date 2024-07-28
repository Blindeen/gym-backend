package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ActivityDoesNotExistException extends CustomException {
    private final String resource = "activity";
    private final String message = "Activity does not exist";
    private final HttpStatus status = HttpStatus.NOT_FOUND;
}
