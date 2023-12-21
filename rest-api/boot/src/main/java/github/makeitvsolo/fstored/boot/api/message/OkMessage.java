package github.makeitvsolo.fstored.boot.api.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public record OkMessage<D>(

        @JsonProperty("status_code")
        int statusCode,

        @JsonProperty("status")
        String status,

        @JsonProperty("data")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<D> data
) {

    public static <T> OkMessage<T> from(final HttpStatus status) {
        return new OkMessage<>(
                status.value(),
                status.getReasonPhrase(),
                Optional.empty()
        );
    }

    public static <T> OkMessage<T> from(final HttpStatus status, final T data) {
        return new OkMessage<>(
                status.value(),
                status.getReasonPhrase(),
                Optional.of(data)
        );
    }
}
