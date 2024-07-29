package project.gym.exception;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@Component
public abstract class CustomException extends RuntimeException {
    protected String resource;
    protected String messageKey;
    protected String message;
    protected HttpStatus status;
    protected static MessageSource messageSource;

    public CustomException() {
        messageKey = getClass().getSimpleName() + ".message";
    }

    public static void setMessageSource(MessageSource messageSource) {
        CustomException.messageSource = messageSource;
    }

    protected void initialize() {
        message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}