package github.makeitvsolo.fstored.storage.minio.exception;

public final class MinioInternalException extends FstoredStorageMinioException {

    public MinioInternalException(final Throwable throwable) {
        super(throwable);
    }
}
