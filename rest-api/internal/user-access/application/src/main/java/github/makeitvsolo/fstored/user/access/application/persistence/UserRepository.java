package github.makeitvsolo.fstored.user.access.application.persistence;

import github.makeitvsolo.fstored.user.access.domain.User;

public interface UserRepository {

    void save(User user);

    boolean existsByName(String name);
}
