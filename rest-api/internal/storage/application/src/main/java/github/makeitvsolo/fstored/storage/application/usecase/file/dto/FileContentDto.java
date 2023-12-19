package github.makeitvsolo.fstored.storage.application.usecase.file.dto;

import java.io.InputStream;

public record FileContentDto(String path, InputStream stream, long size) {
}
