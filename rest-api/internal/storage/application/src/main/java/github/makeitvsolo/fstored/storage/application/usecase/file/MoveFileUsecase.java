package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.MoveFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;

public final class MoveFileUsecase<H extends FileHandle> {

    private final FileStorage<H> storage;
    private final ComposeFileHandle<H> fileHandle;

    public MoveFileUsecase(final FileStorage<H> storage, final ComposeFileHandle<H> fileHandle) {
        this.storage = storage;
        this.fileHandle = fileHandle;
    }

    public void invoke(final MoveFileDto payload) {
        var source = fileHandle.composeAsFile(payload.root(), payload.source())
                .unwrapOrElseThrow(WrongFileHandleException::new);

        var destination = fileHandle.composeAsFile(payload.root(), payload.destination())
                .unwrapOrElseThrow(WrongFileHandleException::new);

        if (!storage.exists(source)) {
            throw new FileDoesNotExistsException();
        }

        if (storage.exists(destination)) {
            throw new FileAlreadyExistsException();
        }

        storage.move(source, destination);
    }
}
