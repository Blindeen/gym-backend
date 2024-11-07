package project.gym.exception.members.confirmation;

import lombok.Getter;
import project.gym.exception.CustomException;

import org.springframework.http.HttpStatus;

@Getter
public class TokenExpiredException extends CustomException {
    public TokenExpiredException() {
        resource = "token";
        status = HttpStatus.GONE;
        initialize();
    }
}
