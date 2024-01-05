package github.makeitvsolo.fstored.core.error.handling.result;

public final class UnwrapException extends RuntimeException {

    UnwrapException(final String details) {
        super(details);
    }
}
