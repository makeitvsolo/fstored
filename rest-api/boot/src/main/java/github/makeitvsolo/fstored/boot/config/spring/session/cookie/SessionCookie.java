package github.makeitvsolo.fstored.boot.config.spring.session.cookie;

import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionCookie {

    public static final String TOKEN_ID = "TOKENID";

    private static final String STRICT_SAME_SITE = "Strict";
    private static final long ZERO_AGE = 0;

    public static ResponseCookie from(final String token, final LocalDateTime expiresAt) {
        return ResponseCookie.from(SessionCookie.TOKEN_ID, token)
                .sameSite(STRICT_SAME_SITE)
                .httpOnly(true)
                .maxAge(Duration.between(expiresAt, LocalDateTime.now()))
                .build();
    }

    public static ResponseCookie remove() {
        return ResponseCookie.from(SessionCookie.TOKEN_ID, "")
                .sameSite(STRICT_SAME_SITE)
                .httpOnly(true)
                .maxAge(ZERO_AGE)
                .build();
    }
}
