package github.makeitvsolo.fstored.boot.api.exception.handling;

import github.makeitvsolo.fstored.boot.api.message.ErrorMessage;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.InvalidUserPasswordException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserAlreadyExistsException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserDoesNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAccessExceptionHandling {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessExceptionHandling.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        LOG.info(String.format("client error occurs: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.from(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler({UserDoesNotExistsException.class, InvalidUserPasswordException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleUnauthorizedException(final Throwable ex) {
        LOG.info(String.format("client error occurs: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }
}
