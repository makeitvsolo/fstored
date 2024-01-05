package github.makeitvsolo.fstored.storage.application.storage.handle;

import github.makeitvsolo.fstored.core.error.handling.result.Result;

public interface ComposeFileHandle<H extends FileHandle> {

    Result<H> composeAsFile(String root, String path);
    Result<H> composeAsFileRelative(String root, String parent, String relativePath);
}
