package github.makeitvsolo.fstored.storage.application.usecase.file.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class FileDoesNotExistsException extends FstoredStorageApplicationException {

    public FileDoesNotExistsException() {
        super("file does not exists");
    }
}
