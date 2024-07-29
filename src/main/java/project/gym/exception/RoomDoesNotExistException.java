package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RoomDoesNotExistException extends CustomException {
    private final String resource = "room";
    private final String message = "Room does not exist";
    private final HttpStatus status = HttpStatus.NOT_FOUND;
}
