package github.makeitvsolo.fstored.storage.minio.exception;

public abstract class FstoredStorageMinioException extends RuntimeException {

    protected FstoredStorageMinioException(final String details) {
        super(details);
    }

    protected FstoredStorageMinioException(final Throwable throwable) {
        super(throwable);
    }
}
