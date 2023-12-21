package github.makeitvsolo.fstored.boot.config.spring.session.exception;

public final class MissingSessionException extends RuntimeException {

    public MissingSessionException() {
        super("missing session cookie");
    }
}
