package github.makeitvsolo.fstored.storage.application.storage;

import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.domain.File;

import java.util.Optional;

public interface FileStorage<H extends FileHandle> {

    void make(H handle);
    void move(H source, H destination);
    void write(H handle, BinarySource source);
    void remove(H handle);

    boolean exists(H handle);
    Optional<File> find(H handle);
}
