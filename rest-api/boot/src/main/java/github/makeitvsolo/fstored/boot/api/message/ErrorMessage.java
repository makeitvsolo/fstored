package github.makeitvsolo.fstored.boot.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public record ErrorMessage(

        @JsonProperty("status_code")
        int statusCode,

        @JsonProperty("error")
        String error,

        @JsonProperty("details")
        String details
) {

    public static ErrorMessage from(final HttpStatus status, final String details) {
        return new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                details
        );
    }
}
