package github.makeitvsolo.fstored.user.access.encoding.bcrypt.exception;

public abstract class FstoredUserAccessEncodingBcryptException extends RuntimeException {

    protected FstoredUserAccessEncodingBcryptException(final String details) {
        super(details);
    }

    protected FstoredUserAccessEncodingBcryptException(final Throwable throwable) {
        super(throwable);
    }
}
