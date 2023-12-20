package github.makeitvsolo.fstored.user.access.persistence.sql.exception;

public abstract class FstoredUserAccessPersistenceSqlException extends RuntimeException {

    protected FstoredUserAccessPersistenceSqlException(final String details) {
        super(details);
    }

    protected FstoredUserAccessPersistenceSqlException(final Throwable throwable) {
        super(throwable);
    }
}
