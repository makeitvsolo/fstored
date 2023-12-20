package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.SessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionAlreadyExpiredException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionDoesNotExistsException;

public final class AuthenticateUserUsecase<S extends SessionToken> {

    private final SessionRepository<S> repository;

    public AuthenticateUserUsecase(final SessionRepository<S> repository) {
        this.repository = repository;
    }

    public UserDto invoke(final String token) {
        var session = repository.findByToken(token)
                .orElseThrow(SessionDoesNotExistsException::new);

        if (session.expired()) {
            throw new SessionAlreadyExpiredException();
        }

        return new UserDto(session.userId(), session.userName());
    }
}
