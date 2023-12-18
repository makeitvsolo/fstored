package github.makeitvsolo.fstored.storage.domain.mapping;

import github.makeitvsolo.fstored.core.mapping.Mapper;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;

import java.util.List;

public interface FolderMapper<R> extends Mapper<R> {

    R into(String path, List<MetaData> resources);
}
