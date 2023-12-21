package github.makeitvsolo.fstored.boot.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public record ErrorMessage(

        @JsonProperty("status_code")
        int statusCode,

        @JsonProperty("error")
        String error,

        @JsonProperty("message")
        String message,

        @JsonProperty("details")
        Optional<String> details
) {

    public static ErrorMessage from(final HttpStatus status, final String message) {
        return new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                message,
                Optional.empty()
        );
    }

    public static ErrorMessage from(final HttpStatus status, final String message, final String details) {
        return new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                message,
                Optional.of(details)
        );
    }
}
