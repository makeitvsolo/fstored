package github.makeitvsolo.fstored.user.access.application.exception;

public abstract class FstoredUserAccessApplicationException extends RuntimeException {

    protected FstoredUserAccessApplicationException(final String details) {
        super(details);
    }

    protected FstoredUserAccessApplicationException(final Throwable throwable) {
        super(throwable);
    }
}
