package github.makeitvsolo.fstored.user.access.application.usecase.access.exception;

import github.makeitvsolo.fstored.user.access.application.exception.FstoredUserAccessApplicationException;

public final class SessionAlreadyExpiredException extends FstoredUserAccessApplicationException {

    public SessionAlreadyExpiredException() {
        super("session already expired");
    }
}
