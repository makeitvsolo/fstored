package github.makeitvsolo.fstored.storage.application.mapping;

import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileContentDto;
import github.makeitvsolo.fstored.storage.domain.mapping.FileMapper;

import java.io.InputStream;

public final class IntoFileContentDtoMapper implements FileMapper<FileContentDto> {

    @Override
    public FileContentDto into(final String path, final long size, final InputStream stream) {
        return new FileContentDto(path, stream, size);
    }
}
