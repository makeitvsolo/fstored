package github.makeitvsolo.fstored.storage.application.usecase.file.dto;

import java.util.List;

public record WriteMultipleFileDto(String root, String path, List<FileSourceDto> files, boolean overwrite) {
}
