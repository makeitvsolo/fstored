package github.makeitvsolo.fstored.storage.domain;

import github.makeitvsolo.fstored.storage.domain.mapping.FolderMapper;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;

import java.util.List;
import java.util.Objects;

public final class Folder {

    private final String root;
    private final String path;
    private final List<MetaData> resources;

    public Folder(final String root, final String path, final List<MetaData> resources) {
        this.root = root;
        this.path = path;
        this.resources = resources;
    }

    public String root() {
        return root;
    }

    public String path() {
        return path;
    }

    public List<MetaData> resources() {
        return resources;
    }

    public <R, M extends FolderMapper<R>> R mapBy(final M mapper) {
        return mapper.into(path, resources);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Folder other)) {
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
