package github.makeitvsolo.fstored.storage.application.storage;

import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;

public interface FolderStorage<H extends FolderHandle> {

    void make(H handle);
    void move(H source, H destination);
    void remove(H handle);

    boolean exists(H handle);
}
