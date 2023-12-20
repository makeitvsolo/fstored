package github.makeitvsolo.fstored.user.access.application.session;

import java.util.Optional;

public interface SessionRepository<S extends SessionToken> {

    void save(S session);
    void remove(S session);

    Optional<S> findByToken(String token);
}
