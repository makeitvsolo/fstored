package github.makeitvsolo.fstored.user.access.session.redis.token;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.session.CreateSessionToken;
import github.makeitvsolo.fstored.user.access.domain.User;

public final class CreateRedisSessionToken implements CreateSessionToken<RedisSessionToken> {

    private final Unique<String> sessionToken;
    private final long timeToLiveInSeconds;

    public CreateRedisSessionToken(final Unique<String> sessionToken, final long timeToLiveInSeconds) {
        this.sessionToken = sessionToken;
        this.timeToLiveInSeconds = timeToLiveInSeconds;
    }

    @Override
    public RedisSessionToken createFor(final User user) {
        return RedisSessionToken.create(
                sessionToken,
                user.id(),
                user.name(),
                timeToLiveInSeconds
        );
    }
}
