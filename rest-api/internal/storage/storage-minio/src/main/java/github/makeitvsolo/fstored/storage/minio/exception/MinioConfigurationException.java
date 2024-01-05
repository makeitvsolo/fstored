package github.makeitvsolo.fstored.storage.minio.exception;

public final class MinioConfigurationException extends FstoredStorageMinioException {

    public MinioConfigurationException(final String details) {
        super(details);
    }
}
