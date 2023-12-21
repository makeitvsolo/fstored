package github.makeitvsolo.fstored.boot.api.exception.handling;

import github.makeitvsolo.fstored.boot.api.message.ErrorMessage;
import github.makeitvsolo.fstored.boot.config.spring.session.cookie.SessionCookie;
import github.makeitvsolo.fstored.boot.config.spring.session.exception.MissingSessionException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionAlreadyExpiredException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionDoesNotExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class WebExceptionHandling {

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleInvalidPayload(final Throwable ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.from(HttpStatus.BAD_REQUEST, "invalid payload", "invalid params"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException ex
    ) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .build();
    }

    @ExceptionHandler({SessionDoesNotExistsException.class, SessionAlreadyExpiredException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleMissingSession(final Throwable ex) {
        var removeCookie = SessionCookie.remove();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, removeCookie.toString())
                .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, "invalid session", ex.getMessage()));
    }

    @ExceptionHandler(MissingSessionException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleMissingSessionCookie(final MissingSessionException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, "missing session"));
    }

    @ExceptionHandler(MultipartException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleMultipartException(final MultipartException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ErrorMessage.from(HttpStatus.PAYLOAD_TOO_LARGE, "uploaded files too large"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> otherwise(final Throwable ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.from(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"));
    }
}
