package github.makeitvsolo.fstored.user.access.application.persistence;

import github.makeitvsolo.fstored.user.access.domain.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    boolean existsByName(String name);
    Optional<User> findByName(String name);
}
