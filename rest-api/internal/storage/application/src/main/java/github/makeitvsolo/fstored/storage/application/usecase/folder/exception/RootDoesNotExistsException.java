package github.makeitvsolo.fstored.storage.application.usecase.folder.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class RootDoesNotExistsException extends FstoredStorageApplicationException {

    public RootDoesNotExistsException() {
        super("root does not exists");
    }
}
