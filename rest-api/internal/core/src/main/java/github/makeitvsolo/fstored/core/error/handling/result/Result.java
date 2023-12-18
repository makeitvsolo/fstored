package github.makeitvsolo.fstored.core.error.handling.result;

import github.makeitvsolo.fstored.core.error.handling.Error;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract sealed class Result<T> permits Ok, Err {

    public abstract boolean isOk();
    public abstract boolean isErr();

    public abstract T unwrap() throws UnwrapException;
    public abstract Error unwrapErr() throws UnwrapException;

    public abstract void ifOk(Consumer<? super T> onOk);
    public abstract void ifOkOrElse(Consumer<? super T> onOk, Consumer<? super Error> onErr);

    public abstract <U> Result<U> map(Function<? super T, ? extends U> okMapper);
    public abstract Result<T> mapErr(Function<? super Error, ? extends Error> errMapper);

    public abstract Result<T> or(Supplier<? extends Result<T>> resultSupplier);

    public abstract T unwrapOr(T other);
    public abstract T unwrapOrElse(Supplier<? extends T> okSupplier);

    public abstract <X extends Throwable> T unwrapOrElseThrow(Function<? super Error, ? extends X> errMapper) throws X;

    public static <U> Result<U> ok(final U value) {
        return new Ok<>(value);
    }

    public static <U> Result<U> err(final Error error) {
        return new Err<>(error);
    }
}
