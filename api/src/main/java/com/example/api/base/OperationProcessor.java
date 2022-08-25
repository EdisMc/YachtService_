package com.example.api.base;

import io.vavr.control.Either;

public interface OperationProcessor<I, R> {
    Either<BaseError, R> process(I input);
}
