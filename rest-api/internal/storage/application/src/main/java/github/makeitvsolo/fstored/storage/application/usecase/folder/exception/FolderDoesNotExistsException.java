package github.makeitvsolo.fstored.storage.application.usecase.folder.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class FolderDoesNotExistsException extends FstoredStorageApplicationException {

    public FolderDoesNotExistsException() {
        super("folder does not exists");
    }
}
