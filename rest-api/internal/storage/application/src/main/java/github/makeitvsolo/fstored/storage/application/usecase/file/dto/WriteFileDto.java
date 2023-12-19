package github.makeitvsolo.fstored.storage.application.usecase.file.dto;

import java.io.InputStream;

public record WriteFileDto(String root, String path, long size, InputStream stream, boolean overwrite) {
}
