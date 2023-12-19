package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;

public final class WriteFileUsecase<H extends FileHandle> {

    private final FileStorage<H> storage;
    private final ComposeFileHandle<H> fileHandle;

    public WriteFileUsecase(final FileStorage<H> storage, final ComposeFileHandle<H> fileHandle) {
        this.storage = storage;
        this.fileHandle = fileHandle;
    }

    public void invoke(final WriteFileDto payload) {
        var handle = fileHandle.composeAsFile(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFileHandleException::new);

        var source = new BinarySource(payload.stream(), payload.size());

        if (!payload.overwrite() && storage.exists(handle)) {
            throw new FileAlreadyExistsException();
        }

        storage.write(handle, source);
    }
}
