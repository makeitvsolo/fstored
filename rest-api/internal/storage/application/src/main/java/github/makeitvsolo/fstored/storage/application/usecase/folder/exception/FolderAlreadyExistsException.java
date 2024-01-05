package github.makeitvsolo.fstored.storage.application.usecase.folder.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class FolderAlreadyExistsException extends FstoredStorageApplicationException {

    public FolderAlreadyExistsException() {
        super("folder already exists");
    }
}
