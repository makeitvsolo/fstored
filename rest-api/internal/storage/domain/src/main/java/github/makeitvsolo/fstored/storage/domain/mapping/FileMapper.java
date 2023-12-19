package github.makeitvsolo.fstored.storage.domain.mapping;

import github.makeitvsolo.fstored.core.mapping.Mapper;

import java.io.InputStream;

public interface FileMapper<R> extends Mapper<R> {

    R into(String path, long size, InputStream stream);
}
