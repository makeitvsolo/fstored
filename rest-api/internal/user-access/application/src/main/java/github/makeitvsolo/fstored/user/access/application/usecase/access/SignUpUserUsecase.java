package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserAlreadyExistsException;
import github.makeitvsolo.fstored.user.access.domain.User;

public final class SignUpUserUsecase {

    private final UserRepository repository;
    private final Unique<String> userId;
    private final Encode security;

    public SignUpUserUsecase(final UserRepository repository, final Unique<String> userId, final Encode security) {
        this.repository = repository;
        this.userId = userId;
        this.security = security;
    }

    public UserDto invoke(final UserCredentialsDto payload) {
        if (repository.existsByName(payload.name())) {
            throw new UserAlreadyExistsException();
        }

        var user = User.create(
                userId,
                payload.name(),
                security.encode(payload.password())
        );

        repository.save(user);
        return new UserDto(user.id(), user.name());
    }
}
