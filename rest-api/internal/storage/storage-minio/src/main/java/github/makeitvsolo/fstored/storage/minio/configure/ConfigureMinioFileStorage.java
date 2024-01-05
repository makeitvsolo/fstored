package github.makeitvsolo.fstored.storage.minio.configure;

import github.makeitvsolo.fstored.storage.minio.MinioFileStorage;
import github.makeitvsolo.fstored.storage.minio.exception.MinioConfigurationException;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;

public final class ConfigureMinioFileStorage {

    private MinioObjectStorage objectStorage;

    ConfigureMinioFileStorage() {

    }

    public static ConfigureMinioFileStorage with() {
        return new ConfigureMinioFileStorage();
    }

    public ConfigureMinioFileStorage objectStorage(final MinioObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
        return this;
    }

    public MinioFileStorage configured() {
        if (objectStorage == null) {
            throw new MinioConfigurationException("missing object storage");
        }

        return new MinioFileStorage(objectStorage);
    }
}
