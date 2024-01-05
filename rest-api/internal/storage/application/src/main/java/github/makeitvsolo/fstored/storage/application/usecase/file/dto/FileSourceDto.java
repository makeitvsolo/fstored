package github.makeitvsolo.fstored.storage.application.usecase.file.dto;

import java.io.InputStream;

public record FileSourceDto(String relativeName, InputStream stream, long size) {
}
