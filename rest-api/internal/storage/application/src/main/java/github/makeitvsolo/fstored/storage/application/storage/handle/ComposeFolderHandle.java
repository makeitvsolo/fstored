package github.makeitvsolo.fstored.storage.application.storage.handle;

import github.makeitvsolo.fstored.core.error.handling.result.Result;

public interface ComposeFolderHandle<H extends FolderHandle> {

    Result<H> composeAsRoot(String root);
    Result<H> composeAsFolder(String root, String path);
}
