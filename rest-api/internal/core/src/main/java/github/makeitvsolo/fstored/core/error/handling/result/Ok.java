package github.makeitvsolo.fstored.core.error.handling.result;

import github.makeitvsolo.fstored.core.error.handling.Error;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

final class Ok<T> extends Result<T> {

    private final T value;

    Ok(final T value) {
        this.value = value;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isErr() {
        return false;
    }

    @Override
    public T unwrap() throws UnwrapException {
        return value;
    }

    @Override
    public Error unwrapErr() throws UnwrapException {
        throw new UnwrapException("Error occurs unwrapping value. Expected value is `Error`, but actual is `Ok`");
    }

    @Override
    public void ifOk(final Consumer<? super T> onOk) {
        onOk.accept(value);
    }

    @Override
    public void ifOkOrElse(final Consumer<? super T> onOk, final Consumer<? super Error> onErr) {
        onOk.accept(value);
    }

    @Override
    public <U> Result<U> map(final Function<? super T, ? extends U> okMapper) {
        return new Ok<>(okMapper.apply(value));
    }

    @Override
    public Result<T> mapErr(final Function<? super Error, ? extends Error> errMapper) {
        return new Ok<>(value);
    }

    @Override
    public Result<T> or(final Supplier<? extends Result<T>> resultSupplier) {
        return this;
    }

    @Override
    public T unwrapOr(final T other) {
        return value;
    }

    @Override
    public T unwrapOrElse(final Supplier<? extends T> okSupplier) {
        return value;
    }

    @Override
    public <X extends Throwable> T unwrapOrElseThrow(final Function<? super Error, ? extends X> errMapper) throws X {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Ok<?> other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
