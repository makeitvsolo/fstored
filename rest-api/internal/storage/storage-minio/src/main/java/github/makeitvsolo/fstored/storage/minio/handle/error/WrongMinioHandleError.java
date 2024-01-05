package github.makeitvsolo.fstored.storage.minio.handle.error;

import github.makeitvsolo.fstored.core.error.handling.Error;

public final class WrongMinioHandleError extends Error {

    public WrongMinioHandleError(final String details) {
        super(details);
    }
}
