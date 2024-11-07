package project.gym.exception.auth;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class PassTypeDoesNotExistException extends CustomException {
    public PassTypeDoesNotExistException() {
        resource = "pass type";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
