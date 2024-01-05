package github.makeitvsolo.fstored.storage.application.mapping;

import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderContentDto;
import github.makeitvsolo.fstored.storage.domain.mapping.FolderMapper;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;

import java.util.List;

public final class IntoFolderContentDtoMapper implements FolderMapper<FolderContentDto> {

    private final IntoMetaDtoMapper metaMapper = new IntoMetaDtoMapper();

    @Override
    public FolderContentDto into(final String path, final List<MetaData> resources) {
        return new FolderContentDto(
                path,
                resources.stream()
                        .map(meta -> meta.mapBy(metaMapper))
                        .toList()
        );
    }
}
