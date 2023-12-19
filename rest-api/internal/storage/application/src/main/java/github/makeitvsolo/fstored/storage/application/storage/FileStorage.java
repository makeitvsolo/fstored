package github.makeitvsolo.fstored.storage.application.storage;

import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;

public interface FileStorage<H extends FileHandle> {

    void write(H handle, BinarySource source);
    void move(H source, H destination);

    boolean exists(H handle);
}
