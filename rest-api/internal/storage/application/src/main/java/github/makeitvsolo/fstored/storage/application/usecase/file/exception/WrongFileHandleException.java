package github.makeitvsolo.fstored.storage.application.usecase.file.exception;

import github.makeitvsolo.fstored.storage.application.exception.FstoredStorageApplicationException;

public final class WrongFileHandleException extends FstoredStorageApplicationException {

    public WrongFileHandleException(final Throwable throwable) {
        super(throwable.getMessage());
    }
}
