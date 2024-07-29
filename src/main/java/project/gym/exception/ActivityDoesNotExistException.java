package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ActivityDoesNotExistException extends CustomException {
    public ActivityDoesNotExistException() {
        resource = "activity";
        messageKey = "exception.activity.not.exist";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
