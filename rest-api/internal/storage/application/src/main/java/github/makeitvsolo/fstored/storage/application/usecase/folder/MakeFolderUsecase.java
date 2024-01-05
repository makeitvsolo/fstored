package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;

public final class MakeFolderUsecase<H extends FolderHandle> {

    private final FolderStorage<H> storage;
    private final ComposeFolderHandle<H> folderHandle;

    public MakeFolderUsecase(final FolderStorage<H> storage, final ComposeFolderHandle<H> folderHandle) {
        this.storage = storage;
        this.folderHandle = folderHandle;
    }

    public void invoke(final FolderDto payload) {
        var handle = folderHandle.composeAsFolder(payload.root(), payload.path())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        if (storage.exists(handle)) {
            throw new FolderAlreadyExistsException();
        }

        storage.make(handle);
    }
}
