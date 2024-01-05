package github.makeitvsolo.fstored.user.access.session.redis.token;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.session.SessionToken;

import java.time.Duration;
import java.time.LocalDateTime;

public final class RedisSessionToken implements SessionToken {

    private static final long ZERO_TIME_TO_LIVE = 0;

    private final String token;
    private final String userId;
    private final String userName;
    private final LocalDateTime expiresAt;

    public RedisSessionToken(
            final String token, final String userId, final String userName, final LocalDateTime expiresAt
    ) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.expiresAt = expiresAt;
    }

    public static RedisSessionToken from(
            final String token, final String userId, final String userName, final long timeToLiveInSeconds
    ) {
        return new RedisSessionToken(
                token,
                userId,
                userName,
                LocalDateTime.now()
                        .plusSeconds(timeToLiveInSeconds)
        );
    }

    public static RedisSessionToken create(
            final Unique<String> token, final String userId, final String userName, final long timeToLiveInSeconds
    ) {
        return from(token.unique(), userId, userName, timeToLiveInSeconds);
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public String userId() {
        return userId;
    }

    @Override
    public String userName() {
        return userName;
    }

    @Override
    public LocalDateTime expiresAt() {
        return expiresAt;
    }

    @Override
    public boolean expired() {
        return LocalDateTime.now()
                .isAfter(expiresAt);
    }

    public long timeToLiveInSeconds(final LocalDateTime current) {
        var duration =  Duration.between(current, expiresAt)
                .toSeconds();

        if (duration <= ZERO_TIME_TO_LIVE) {
            return ZERO_TIME_TO_LIVE;
        }

        return duration;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof RedisSessionToken other)) {
            return false;
        }

        return token.equals(other.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
