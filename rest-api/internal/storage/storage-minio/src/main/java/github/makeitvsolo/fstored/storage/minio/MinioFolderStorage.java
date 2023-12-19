package github.makeitvsolo.fstored.storage.minio;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.domain.Folder;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;

import java.util.List;
import java.util.Optional;

public final class MinioFolderStorage implements FolderStorage<MinioHandle> {

    private final MinioObjectStorage objectStorage;

    public MinioFolderStorage(final MinioObjectStorage objectStorage) {
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

        var recursive = true;
        var objects = objectStorage.findObjects(source.objectName(), recursive);

        objects.forEach(handle -> {
            var movedHandle = handle.softMove(source, destination).unwrap();

            objectStorage.copy(handle, movedHandle);
        });

        objectStorage.removeMultiple(objects);
    }

    @Override
    public void remove(final MinioHandle handle) {
        var recursive = true;
        var objects = objectStorage.findObjects(handle.objectName(), recursive);

        objectStorage.removeMultiple(objects);
    }

    @Override
    public boolean exists(final MinioHandle handle) {
        var recursive = false;
        var objects = objectStorage.findObjects(handle.objectName(), recursive);

        return !objects.isEmpty();
    }

    @Override
    public List<MetaData> findRecursively(final MinioHandle handle, final String name) {
        return objectStorage.findMatches(handle, name);
    }

    @Override
    public Optional<Folder> find(final MinioHandle handle) {
        return objectStorage.findFolder(handle);
    }
}
