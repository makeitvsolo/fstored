package github.makeitvsolo.fstored.storage.application.usecase.folder.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class WrongFolderHandleException extends FstoredStorageApplicationException {

    public WrongFolderHandleException(final Throwable throwable) {
        super(throwable);
    }
}
