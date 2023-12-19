package github.makeitvsolo.fstored.storage.application.usecase.folder.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record MetaDto(

        String path,
        String resource,
        Optional<Long> size,
        Optional<LocalDateTime> modificationTime
) {
}
