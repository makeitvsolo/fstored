package github.makeitvsolo.fstored.user.access.application.session;

import java.time.LocalDateTime;

public final class TestSessionToken implements SessionToken {

    private final String token;
    private final String userId;
    private final String userName;
    private final LocalDateTime expiresAt;
    private final boolean expired;

    public TestSessionToken(
            final String token,
            final String userId,
            final String userName,
            final LocalDateTime expiresAt,
            final boolean expired
    ) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.expiresAt = expiresAt;
        this.expired = expired;
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
        return expired;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TestSessionToken other)) {
            return false;
        }

        return token.equals(other.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
