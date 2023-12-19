package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;

public final class MountRootFolderUsecase<H extends FolderHandle> {

    private final FolderStorage<H> storage;
    private final ComposeFolderHandle<H> folderHandle;

    public MountRootFolderUsecase(final FolderStorage<H> storage, final ComposeFolderHandle<H> folderHandle) {
        this.storage = storage;
        this.folderHandle = folderHandle;
    }

    public void invoke(final String root) {
        var handle = folderHandle.composeAsRoot(root)
                .unwrapOrElseThrow(WrongFolderHandleException::new);

        storage.make(handle);
    }
}
