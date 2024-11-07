package project.gym.exception.activities;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class RoomDoesNotExistException extends CustomException {
    public RoomDoesNotExistException() {
        resource = "room";
        status = HttpStatus.NOT_FOUND;
        initialize();
    }
}
