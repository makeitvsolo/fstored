package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderSearchDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MatchingObjectsDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MetaDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.RootSearchDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.RootDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;

public final class FolderSearchUsecase<H extends FolderHandle> {

    private final FolderStorage<H> storage;
    private final ComposeFolderHandle<H> folderHandle;
    private final MetaMapper<MetaDto> mapper;

    public FolderSearchUsecase(
            final FolderStorage<H> storage,
            final ComposeFolderHandle<H> folderHandle,
            final MetaMapper<MetaDto> mapper
    ) {
        this.storage = storage;
        this.folderHandle = folderHandle;
        this.mapper = mapper;
    }

    public MatchingObjectsDto invoke(final RootSearchDto payload) {
        var handle = folderHandle.composeAsRoot(payload.root())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        if (!storage.exists(handle)) {
            throw new RootDoesNotExistsException();
        }

        var objects = storage.findRecursively(handle, payload.prefix())
                .stream()
                .map(meta -> meta.mapBy(mapper))
                .toList();

        return new MatchingObjectsDto(objects);
    }

    public MatchingObjectsDto invoke(final FolderSearchDto payload) {
        var handle = folderHandle.composeAsFolder(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        if (!storage.exists(handle)) {
            throw new FolderDoesNotExistsException();
        }

        var objects = storage.findRecursively(handle, payload.prefix())
                .stream()
                .map(meta -> meta.mapBy(mapper))
                .toList();

        return new MatchingObjectsDto(objects);
    }
}
