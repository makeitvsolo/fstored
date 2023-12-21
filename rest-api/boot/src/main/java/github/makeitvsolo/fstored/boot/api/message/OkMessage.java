package github.makeitvsolo.fstored.boot.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public record OkMessage<D>(

        @JsonProperty("status_code")
        int statusCode,

        @JsonProperty("status")
        String status,

        @JsonProperty("data")
        D data
) {

    public static <T> OkMessage<T> from(final HttpStatus status, final T data) {
        return new OkMessage<>(
                status.value(),
                status.getReasonPhrase(),
                data
        );
    }
}
