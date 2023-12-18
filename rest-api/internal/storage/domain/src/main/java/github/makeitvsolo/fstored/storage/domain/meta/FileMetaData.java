package github.makeitvsolo.fstored.storage.domain.meta;

import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;

import java.time.LocalDateTime;

public final class FileMetaData extends MetaData {

    private static final String FILE_RESOURCE = "files";

    private final long size;
    private final LocalDateTime modifiedAt;

    public FileMetaData(final String root, final String path, final long size, final LocalDateTime modifiedAt) {
        super(root, path);
        this.size = size;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String resource() {
        return FILE_RESOURCE;
    }

    public long size() {
        return size;
    }

    public LocalDateTime modifiedAt() {
        return modifiedAt;
    }

    @Override
    public <R, M extends MetaMapper<R>> R mapBy(final M mapper) {
        return mapper.into(path(), FILE_RESOURCE, size, modifiedAt);
    }
}
