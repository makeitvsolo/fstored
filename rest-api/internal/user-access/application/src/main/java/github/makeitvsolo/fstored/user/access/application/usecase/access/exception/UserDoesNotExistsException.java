package github.makeitvsolo.fstored.user.access.application.usecase.access.exception;

import github.makeitvsolo.fstored.user.access.application.exception.FstoredUserAccessApplicationException;

public final class UserDoesNotExistsException extends FstoredUserAccessApplicationException {

    public UserDoesNotExistsException() {
        super("user does not exists");
    }
}
