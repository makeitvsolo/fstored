package github.makeitvsolo.fstored.storage.application.usecase.file.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class FileAlreadyExistsException extends FstoredStorageApplicationException {

    public FileAlreadyExistsException() {
        super("file already exists");
    }
}
