package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.SessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionDoesNotExistsException;

public final class SignOutUserUsecase<S extends SessionToken> {

    private final SessionRepository<S> repository;

    public SignOutUserUsecase(final SessionRepository<S> repository) {
        this.repository = repository;
    }

    public void invoke(final String token) {
        var session = repository.findByToken(token)
                .orElseThrow(SessionDoesNotExistsException::new);

        repository.remove(session);
    }
}
