package github.makeitvsolo.fstored.storage.minio.configure;

import github.makeitvsolo.fstored.storage.minio.MinioFolderStorage;
import github.makeitvsolo.fstored.storage.minio.exception.MinioConfigurationException;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;

public final class ConfigureMinioFolderStorage {

    private MinioObjectStorage objectStorage;

    ConfigureMinioFolderStorage() {

    }

    public static ConfigureMinioFolderStorage with() {
        return new ConfigureMinioFolderStorage();
    }

    public ConfigureMinioFolderStorage objectStorage(final MinioObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
        return this;
    }

    public MinioFolderStorage configured() {
        if (objectStorage == null) {
            throw new MinioConfigurationException("missing object storage");
        }

        return new MinioFolderStorage(objectStorage);
    }
}
