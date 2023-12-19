package github.makeitvsolo.fstored.storage.minio.handle;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.minio.handle.error.WrongMinioHandleError;

public final class ComposeMinioHandle
        implements ComposeFolderHandle<MinioHandle>, ComposeFileHandle<MinioHandle> {

    private static final String ROOT_REGEX = "^[0-9a-zA-Z-_.]+$";
    private static final String FOLDER_PATH_REGEX = "^(/([0-9a-zA-Z-_.]+/)*)$";
    private static final String FILE_PATH_REGEX = "^/([0-9a-zA-Z-_.]+)(/([0-9a-zA-Z-_.]+))*$";
    private static final String RELATIVE_FILE_PATH_REGEX = "^[^/][0-9a-zA-Z-_./]+[^/]$";

    private static final String PATH_DELIMITER = "/";

    @Override
    public Result<MinioHandle> composeAsRoot(final String root) {
        if (!root.matches(ROOT_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid root name"));
        }

        var objectName = root.concat(PATH_DELIMITER);
        return Result.ok(new MinioHandle(objectName));
    }

    @Override
    public Result<MinioHandle> composeAsFolder(final String root, final String path) {
        if (!root.matches(ROOT_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid root name"));
        }

        if (!path.matches(FOLDER_PATH_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid folder path"));
        }

        var objectName = root.concat(path);
        return Result.ok(new MinioHandle(objectName));
    }

    @Override
    public Result<MinioHandle> composeAsFile(final String root, final String path) {
        if (!root.matches(ROOT_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid root name"));
        }

        if (!path.matches(FILE_PATH_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid file path"));
        }

        var objectName = root.concat(path);
        return Result.ok(new MinioHandle(objectName));
    }

    @Override
    public Result<MinioHandle> composeAsFileRelative(
            final String root, final String parent, final String relativePath
    ) {
        if (!root.matches(ROOT_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid root name"));
        }

        if (!parent.matches(FOLDER_PATH_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid folder path"));
        }

        if (!relativePath.matches(RELATIVE_FILE_PATH_REGEX)) {
            return Result.err(new WrongMinioHandleError("invalid file relative name"));
        }

        var objectName = root.concat(parent).concat(relativePath);
        return Result.ok(new MinioHandle(objectName));
    }
}
