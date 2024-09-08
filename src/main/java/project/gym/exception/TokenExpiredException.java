package project.gym.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TokenExpiredException extends CustomException {
    public TokenExpiredException() {
        resource = "token";
        status = HttpStatus.GONE;
        initialize();
    }
}
