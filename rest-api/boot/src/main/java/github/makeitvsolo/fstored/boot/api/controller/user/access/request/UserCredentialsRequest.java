package github.makeitvsolo.fstored.boot.api.controller.user.access.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCredentialsRequest(

        @NotNull
        @Size(min = MIN_NAME_SIZE)
        String name,

        @NotNull
        @Size(min = MIN_PASSWORD_SIZE)
        String password
) {

    private static final int MIN_NAME_SIZE = 5;
    private static final int MIN_PASSWORD_SIZE = 5;
}
