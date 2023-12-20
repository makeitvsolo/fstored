package github.makeitvsolo.fstored.user.access.application.session;

import github.makeitvsolo.fstored.user.access.domain.User;

public interface CreateSessionToken<S extends SessionToken> {

    S createFor(User user);
}
