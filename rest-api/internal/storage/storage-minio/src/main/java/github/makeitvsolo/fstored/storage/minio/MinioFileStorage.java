package github.makeitvsolo.fstored.storage.minio;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.domain.File;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;

import java.util.Map;
import java.util.Optional;

public final class MinioFileStorage implements FileStorage<MinioHandle> {

    private final MinioObjectStorage objectStorage;

    public MinioFileStorage(final MinioObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    @Override
    public void make(final MinioHandle handle) {
        objectStorage.make(handle);
    }

    @Override
    public void move(final MinioHandle source, final MinioHandle destination) {
        if (source.objectName().equals(destination.objectName())) {
            return;
        }

        objectStorage.copy(source, destination);
        objectStorage.remove(source);
    }

    @Override
    public void write(final MinioHandle handle, final BinarySource source) {
        objectStorage.write(handle, source);
    }

    @Override
    public void writeMultiple(final Map<MinioHandle, BinarySource> sources) {
        objectStorage.writeMultiple(sources);
    }

    @Override
    public void remove(final MinioHandle handle) {
        objectStorage.remove(handle);
    }

    @Override
    public boolean exists(final MinioHandle handle) {
        return objectStorage.exists(handle);
    }

    @Override
    public Optional<File> find(final MinioHandle handle) {
        return objectStorage.findFile(handle);
    }
}
