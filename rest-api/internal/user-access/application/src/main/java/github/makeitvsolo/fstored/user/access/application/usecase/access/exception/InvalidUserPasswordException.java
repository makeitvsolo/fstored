package github.makeitvsolo.fstored.user.access.application.usecase.access.exception;

import github.makeitvsolo.fstored.user.access.application.exception.FstoredUserAccessApplicationException;

public final class InvalidUserPasswordException extends FstoredUserAccessApplicationException {

    public InvalidUserPasswordException() {
        super("invalid password");
    }
}
