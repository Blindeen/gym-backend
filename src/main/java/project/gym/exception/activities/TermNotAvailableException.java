package project.gym.exception.activities;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class TermNotAvailableException extends CustomException {
    public TermNotAvailableException() {
        resource = "activity";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
