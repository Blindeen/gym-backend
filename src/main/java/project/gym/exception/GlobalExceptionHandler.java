package project.gym.exception;

import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request) {

        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request) {
        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("method", List.of(ex.getMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();

        String code;
        String[] args;
        Locale locale = LocaleContextHolder.getLocale();

        String parameterName = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null) {
            code = "MethodArgumentTypeMismatchException.extended.message";
            args = new String[] { parameterName, requiredType.getSimpleName() };
        } else {
            code = "MethodArgumentTypeMismatchException.basic.message";
            args = new String[] { parameterName };
        }

        String message = messageSource.getMessage(code, args, locale);
        errors.put("invalid argument", List.of(message));
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("authentication", List.of(ex.getMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("token", List.of(ex.getMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        Map<String, Map<String, List<String>>> body = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(ex.getResource(), List.of(ex.getMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherException() {
        Map<String, Object> body = new HashMap<>();
        body.put("error", messageSource.getMessage(
                "InternalServerException.message",
                null,
                LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
