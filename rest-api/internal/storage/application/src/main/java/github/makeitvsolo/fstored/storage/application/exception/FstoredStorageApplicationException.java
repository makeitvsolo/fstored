package github.makeitvsolo.fstored.storage.application.exception;

public abstract class FstoredStorageApplicationException extends RuntimeException {

    protected FstoredStorageApplicationException(final String details) {
        super(details);
    }

    protected FstoredStorageApplicationException(final Throwable throwable) {
        super(throwable);
    }
}
