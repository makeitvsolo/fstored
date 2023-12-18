package github.makeitvsolo.fstored.core.error.handling.result;

import github.makeitvsolo.fstored.core.error.handling.Error;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

final class Err<T> extends Result<T> {

    private final Error error;

    Err(final Error error) {
        this.error = error;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isErr() {
        return true;
    }

    @Override
    public T unwrap() throws UnwrapException {
        throw new UnwrapException("Error occurs unwrapping value. Expected value is `Ok`, but actual is `Err`");
    }

    @Override
    public Error unwrapErr() throws UnwrapException {
        return error;
    }

    @Override
    public void ifOk(final Consumer<? super T> onOk) {
        return;
    }

    @Override
    public void ifOkOrElse(final Consumer<? super T> onOk, final Consumer<? super Error> onErr) {
        onErr.accept(error);
    }

    @Override
    public <U> Result<U> map(final Function<? super T, ? extends U> okMapper) {
        return new Err<>(error);
    }

    @Override
    public Result<T> mapErr(final Function<? super Error, ? extends Error> errMapper) {
        return new Err<>(errMapper.apply(error));
    }

    @Override
    public Result<T> or(final Supplier<? extends Result<T>> resultSupplier) {
        return resultSupplier.get();
    }

    @Override
    public T unwrapOr(final T other) {
        return other;
    }

    @Override
    public T unwrapOrElse(final Supplier<? extends T> okSupplier) {
        return okSupplier.get();
    }

    @Override
    public <X extends Throwable> T unwrapOrElseThrow(final Function<? super Error, ? extends X> errMapper) throws X {
        throw errMapper.apply(error);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Err<?> other)) {
            return false;
        }

        return error.equals(other.error);
    }

    @Override
    public int hashCode() {
        return error.hashCode();
    }
}
