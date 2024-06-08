package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    protected String resource;
    protected String message;
    protected HttpStatus status;
}
