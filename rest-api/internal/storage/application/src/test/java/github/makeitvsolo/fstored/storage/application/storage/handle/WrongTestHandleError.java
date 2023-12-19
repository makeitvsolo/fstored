package github.makeitvsolo.fstored.storage.application.storage.handle;

import github.makeitvsolo.fstored.core.error.handling.Error;

public final class WrongTestHandleError extends Error {

    public WrongTestHandleError() {
        super("wrong test handle");
    }
}
