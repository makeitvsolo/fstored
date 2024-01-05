package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileContentDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import github.makeitvsolo.fstored.storage.domain.mapping.FileMapper;

public final class FetchFileUsecase<H extends FileHandle> {

    private final FileStorage<H> storage;
    private final ComposeFileHandle<H> fileHandle;
    private final FileMapper<FileContentDto> mapper;

    public FetchFileUsecase(
            final FileStorage<H> storage,
            final ComposeFileHandle<H> fileHandle,
            final FileMapper<FileContentDto> mapper
    ) {
        this.storage = storage;
        this.fileHandle = fileHandle;
        this.mapper = mapper;
    }

    public FileContentDto invoke(final FileDto payload) {
        var handle = fileHandle.composeAsFile(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFileHandleException::new);

        return storage.find(handle)
                .map(file -> file.mapBy(mapper))
                .orElseThrow(FileDoesNotExistsException::new);
    }
}
