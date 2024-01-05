package github.makeitvsolo.fstored.storage.application.usecase.folder.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class RootAlreadyExistsException extends FstoredStorageApplicationException {

    public RootAlreadyExistsException() {
        super("root already exists");
    }
}
