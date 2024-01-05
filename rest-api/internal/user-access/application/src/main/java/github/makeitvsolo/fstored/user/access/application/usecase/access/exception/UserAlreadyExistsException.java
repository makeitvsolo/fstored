package github.makeitvsolo.fstored.user.access.application.usecase.access.exception;

import github.makeitvsolo.fstored.user.access.application.exception.FstoredUserAccessApplicationException;

public final class UserAlreadyExistsException extends FstoredUserAccessApplicationException {

    public UserAlreadyExistsException() {
        super("user already exists");
    }
}
