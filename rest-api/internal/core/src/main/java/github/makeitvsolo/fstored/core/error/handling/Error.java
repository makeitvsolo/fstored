package github.makeitvsolo.fstored.core.error.handling;

public abstract class Error extends RuntimeException {

    protected Error(final String details) {
        super(details);
    }

    protected Error(final Throwable throwable) {
        super(throwable);
    }
}
