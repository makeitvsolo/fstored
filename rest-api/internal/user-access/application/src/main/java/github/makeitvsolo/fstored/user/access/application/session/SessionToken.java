package github.makeitvsolo.fstored.user.access.application.session;

import java.time.LocalDateTime;

public interface SessionToken {

    String token();
    String userId();
    String userName();
    LocalDateTime expiresAt();

    boolean expired();
}
