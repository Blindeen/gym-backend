package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PassTypeDoesNotExistException extends CustomException {
    public PassTypeDoesNotExistException() {
        resource = "pass type";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
