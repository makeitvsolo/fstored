package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MoveFolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;

public final class MoveFolderUsecase<H extends FolderHandle> {

    private final FolderStorage<H> storage;
    private final ComposeFolderHandle<H> folderHandle;

    public MoveFolderUsecase(final FolderStorage<H> storage, final ComposeFolderHandle<H> folderHandle) {
        this.storage = storage;
        this.folderHandle = folderHandle;
    }

    public void invoke(final MoveFolderDto payload) {
        var source = folderHandle.composeAsFolder(payload.root(), payload.source())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        var destination = folderHandle.composeAsFolder(payload.root(), payload.destination())
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        if (!storage.exists(source)) {
            throw new FolderDoesNotExistsException();
        }

        if (storage.exists(destination)) {
            throw new FolderAlreadyExistsException();
        }

        storage.move(source, destination);
    }
}
