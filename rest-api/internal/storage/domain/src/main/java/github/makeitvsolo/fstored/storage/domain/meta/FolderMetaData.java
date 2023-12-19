package github.makeitvsolo.fstored.storage.domain.meta;

import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;

public final class FolderMetaData extends MetaData {

    private static final String FOLDER_RESOURCE = "folders";

    public FolderMetaData(final String root, final String path) {
        super(root, path);
    }

    @Override
    public String resource() {
        return FOLDER_RESOURCE;
    }

    @Override
    public <R, M extends MetaMapper<R>> R mapBy(final M mapper) {
        return mapper.into(path(), FOLDER_RESOURCE);
    }
}
