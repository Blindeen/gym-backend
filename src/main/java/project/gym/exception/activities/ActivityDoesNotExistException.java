package project.gym.exception.activities;

import lombok.Getter;
import project.gym.exception.CustomException;

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
