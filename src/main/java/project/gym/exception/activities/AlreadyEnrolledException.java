package project.gym.exception.activities;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class AlreadyEnrolledException extends CustomException {
    public AlreadyEnrolledException() {
        resource = "activity";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
