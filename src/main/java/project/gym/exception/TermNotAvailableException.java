package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TermNotAvailableException extends CustomException {
    public TermNotAvailableException() {
        resource = "activity";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
