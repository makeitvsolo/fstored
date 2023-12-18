package github.makeitvsolo.fstored.storage.domain.mapping;

import github.makeitvsolo.fstored.core.mapping.Mapper;

import java.time.LocalDateTime;

public interface MetaMapper<R> extends Mapper<R> {

    R into(String path, String resource);
    R into(String path, String resource, long size, LocalDateTime modifiedAt);
}
