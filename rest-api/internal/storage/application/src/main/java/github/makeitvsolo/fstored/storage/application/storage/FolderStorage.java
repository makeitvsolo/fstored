package github.makeitvsolo.fstored.storage.application.storage;

import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;

public interface FolderStorage<H extends FolderHandle> {

    void make(H handle);
    void move(H source, H destination);

    boolean exists(H handle);
}
