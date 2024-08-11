package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyEnrolledException extends CustomException {
    public AlreadyEnrolledException() {
        resource = "activity";
        status = HttpStatus.BAD_REQUEST;
        initialize();
    }
}
