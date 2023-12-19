package github.makeitvsolo.fstored.storage.application.usecase.folder.dto;

import java.util.List;

public record FolderContentDto(String path, List<MetaDto> children) {
}
