package github.makeitvsolo.fstored.storage.application.storage;

import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.domain.Folder;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;

import java.util.List;
import java.util.Optional;

public interface FolderStorage<H extends FolderHandle> {

    void make(H handle);
    void move(H source, H destination);
    void remove(H handle);

    boolean exists(H handle);
    List<MetaData> findRecursively(H handle, String name);
    Optional<Folder> find(H handle);
}
