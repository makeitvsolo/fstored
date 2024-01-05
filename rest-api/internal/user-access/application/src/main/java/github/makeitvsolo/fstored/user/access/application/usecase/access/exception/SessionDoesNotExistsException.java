package github.makeitvsolo.fstored.user.access.application.usecase.access.exception;

import github.makeitvsolo.fstored.user.access.application.exception.FstoredUserAccessApplicationException;

public final class SessionDoesNotExistsException extends FstoredUserAccessApplicationException {

    public SessionDoesNotExistsException() {
        super("session does not exists");
    }
}
