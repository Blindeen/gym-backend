package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RoomDoesNotExistException extends CustomException {
    public RoomDoesNotExistException() {
        resource = "room";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
