package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.application.session.CreateSessionToken;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.SessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.AccessTokenDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.InvalidUserPasswordException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserDoesNotExistsException;

public final class SignInUserUsecase<S extends SessionToken> {

    private final UserRepository userRepository;
    private final SessionRepository<S> sessionRepository;
    private final CreateSessionToken<S> userSession;
    private final Encode security;

    public SignInUserUsecase(
            final UserRepository userRepository,
            final SessionRepository<S> sessionRepository,
            final CreateSessionToken<S> userSession,
            final Encode security
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.userSession = userSession;
        this.security = security;
    }

    public AccessTokenDto invoke(final UserCredentialsDto payload) {
        var user = userRepository.findByName(payload.name())
                .orElseThrow(UserDoesNotExistsException::new);

        if (!security.matches(payload.password(), user.password())) {
            throw new InvalidUserPasswordException();
        }

        var session = userSession.createFor(user);
        sessionRepository.save(session);

        return new AccessTokenDto(
                session.token(), session.expiresAt(), new UserDto(user.id(), user.name())
        );
    }
}
