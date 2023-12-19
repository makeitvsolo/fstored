package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderContentDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import github.makeitvsolo.fstored.storage.domain.mapping.FolderMapper;

public final class FetchFolderUsecase<H extends FolderHandle> {

    private final FolderStorage<H> storage;
    private final ComposeFolderHandle<H> folderHandle;
    private final FolderMapper<FolderContentDto> mapper;

    public FetchFolderUsecase(
            final FolderStorage<H> storage,
            final ComposeFolderHandle<H> folderHandle,
            final FolderMapper<FolderContentDto> mapper
    ) {
        this.storage = storage;
        this.folderHandle = folderHandle;
        this.mapper = mapper;
    }

    public FolderContentDto invoke(final String root) {
        var handle = folderHandle.composeAsRoot(root)
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        return storage.find(handle)
                .map(folder -> folder.mapBy(mapper))
                .orElseThrow(FolderDoesNotExistsException::new);
    }

    public FolderContentDto invoke(final FolderDto payload) {
        var handle = folderHandle.composeAsFolder(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        return storage.find(handle)
                .map(folder -> folder.mapBy(mapper))
                .orElseThrow(FolderDoesNotExistsException::new);
    }
}
