package github.makeitvsolo.fstored.user.access.domain.mapping;

import github.makeitvsolo.fstored.core.mapping.Mapper;

public interface UserMapper<R> extends Mapper<R> {

    R into(String id, String name);
}
