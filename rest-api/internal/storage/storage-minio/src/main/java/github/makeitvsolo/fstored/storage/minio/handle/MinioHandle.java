package github.makeitvsolo.fstored.storage.minio.handle;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.storage.handle.FileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.FolderHandle;
import github.makeitvsolo.fstored.storage.minio.handle.error.WrongMinioHandleError;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class MinioHandle implements FolderHandle, FileHandle {

    private static final String OBJECT_NAME_REGEX = "^(([0-9a-zA-Z-_.]+/)([0-9a-zA-Z-_.]+/?)*)$";

    private static final String PATH_DELIMITER = "/";
    private static final String ROOT_PATH = "/";
    private static final int MIN_NAME_PARTS = 1;

    private final String objectName;

    public MinioHandle(final String objectName) {
        this.objectName = objectName;
    }

    public static Result<MinioHandle> fromObjectName(final String objectName) {
        if (!objectName.matches(OBJECT_NAME_REGEX)) {
            return Result.err(new WrongMinioHandleError("wrong object"));
        }

        return Result.ok(new MinioHandle(objectName));
    }

    public String objectName() {
        return objectName;
    }

    public String root() {
        return Arrays.stream(objectName.split(PATH_DELIMITER))
                .findFirst()
                .get();
    }

    public String path() {
        var objectNameParts = objectName.split(PATH_DELIMITER);

        if (objectNameParts.length <= MIN_NAME_PARTS) {
            return ROOT_PATH;
        }

        return isFolder()
                ? Arrays.stream(objectNameParts)
                .skip(1)
                .collect(Collectors.joining(PATH_DELIMITER, ROOT_PATH, PATH_DELIMITER))

                : Arrays.stream(objectNameParts)
                .skip(1)
                .collect(Collectors.joining(PATH_DELIMITER, ROOT_PATH, ""));
    }

    public boolean isFolder() {
        return objectName.endsWith(PATH_DELIMITER);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MinioHandle other)) {
            return false;
        }

        return objectName.equals(other.objectName);
    }

    @Override
    public int hashCode() {
        return objectName.hashCode();
    }
}
