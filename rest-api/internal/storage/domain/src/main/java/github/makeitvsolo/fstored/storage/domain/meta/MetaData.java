package github.makeitvsolo.fstored.storage.domain.meta;

import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;

import java.util.Objects;

public abstract sealed class MetaData permits FileMetaData, FolderMetaData {

    private final String root;
    private final String path;

    protected MetaData(final String root, final String path) {
        this.root = root;
        this.path = path;
    }

    public String root() {
        return root;
    }

    public String path() {
        return path;
    }

    public abstract String resource();

    public abstract <R, M extends MetaMapper<R>> R mapBy(M mapper);

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MetaData other)) {
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
