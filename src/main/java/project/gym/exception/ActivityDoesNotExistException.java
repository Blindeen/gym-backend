package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ActivityDoesNotExistException extends CustomException {
    public ActivityDoesNotExistException() {
        super();
        resource = "activity";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
