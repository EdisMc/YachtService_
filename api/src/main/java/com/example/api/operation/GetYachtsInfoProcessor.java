package com.example.api.operation;

import com.example.api.base.BaseError;
import com.example.api.base.OperationProcessor;
import com.example.api.model.FindYachtRequest;
import com.example.api.model.FindYachtResponse;
import io.vavr.control.Either;


public interface GetYachtsInfoProcessor extends OperationProcessor<FindYachtRequest, FindYachtResponse> {
    Either<BaseError, FindYachtResponse> process(FindYachtRequest findCarsRequest);
}
