package com.example.api.operation;

import com.example.api.base.BaseError;
import com.example.api.base.OperationProcessor;
import com.example.api.model.SortYachtResponse;
import com.example.api.model.SortYachtsRequest;
import io.vavr.control.Either;


public interface SortYachtsProcessor extends OperationProcessor<SortYachtsRequest, SortYachtResponse> {
    Either<BaseError, SortYachtResponse> process(SortYachtsRequest sortCarsRequest);
}
