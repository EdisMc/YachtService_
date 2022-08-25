package com.example.api.operation;

import com.example.api.base.BaseError;
import com.example.api.base.OperationProcessor;
import com.example.api.model.ReturnYachtRequest;
import com.example.api.model.ReturnYachtResponse;
import io.vavr.control.Either;

public interface ReturnYachtProcessor extends OperationProcessor<ReturnYachtRequest, ReturnYachtResponse> {
    Either<BaseError, ReturnYachtResponse> process(ReturnYachtRequest returnYachtRequest);
}
