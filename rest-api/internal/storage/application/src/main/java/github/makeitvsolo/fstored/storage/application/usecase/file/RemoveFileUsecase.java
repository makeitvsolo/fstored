package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;

public final class RemoveFileUsecase<H extends FileHandle> {

    private final FileStorage<H> storage;
    private final ComposeFileHandle<H> fileHandle;

    public RemoveFileUsecase(final FileStorage<H> storage, final ComposeFileHandle<H> fileHandle) {
        this.storage = storage;
        this.fileHandle = fileHandle;
    }

    public void invoke(final FileDto payload) {
        var handle = fileHandle.composeAsFile(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFileHandleException::new);

        if (!storage.exists(handle)) {
            throw new FileDoesNotExistsException();
        }

        storage.remove(handle);
    }
}
