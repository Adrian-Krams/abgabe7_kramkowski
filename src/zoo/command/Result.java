package zoo.command;

public sealed interface Result<E, R> permits Result.Success, Result.Failure {

    record Success<E, R>(R value) implements Result<E, R> {
    }

    record Failure<E, R>(E error) implements Result<E, R> {
    }

    static <E, R> Result<E, R> success(R value) {
        return new Success<>(value);
    }

    static <E, R> Result<E, R> failure(E error) {
        return new Failure<>(error);
    }
}