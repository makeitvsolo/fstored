package github.makeitvsolo.fstored.user.access.session.redis.exception;

public abstract class FstoredUserAccessSessionRedisException extends RuntimeException {

    protected FstoredUserAccessSessionRedisException(final String message) {
        super(message);
    }

    protected FstoredUserAccessSessionRedisException(final Throwable throwable) {
        super(throwable);
    }
}
