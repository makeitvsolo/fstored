package github.makeitvsolo.fstored.storage.domain;

import github.makeitvsolo.fstored.storage.domain.mapping.FileMapper;

import java.io.InputStream;
import java.util.Objects;

public final class File {

    private final String root;
    private final String path;
    private final long size;
    private final InputStream stream;

    public File(final String root, final String path, final long size, final InputStream stream) {
        this.root = root;
        this.path = path;
        this.size = size;
        this.stream = stream;
    }

    public String root() {
        return root;
    }

    public String path() {
        return path;
    }

    public long size() {
        return size;
    }

    public InputStream stream() {
        return stream;
    }

    public <R, M extends FileMapper<R>> R mapBy(final M mapper) {
        return mapper.into(path, size, stream);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof File other)) {
            return false;
        }

        return root.equals(other.root)
                && path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, path);
    }
}
