package github.makeitvsolo.fstored.storage.application.mapping;

import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MetaDto;
import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;

import java.time.LocalDateTime;
import java.util.Optional;

public final class IntoMetaDtoMapper implements MetaMapper<MetaDto> {

    @Override
    public MetaDto into(final String path, final String resource) {
        return new MetaDto(path, resource, Optional.empty(), Optional.empty());
    }

    @Override
    public MetaDto into(final String path, final String resource, final long size, final LocalDateTime modifiedAt) {
        return new MetaDto(path, resource, Optional.of(size), Optional.of(modifiedAt));
    }
}
