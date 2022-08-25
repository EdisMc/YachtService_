package com.example.api.operation;

import com.example.api.base.BaseError;
import com.example.api.base.OperationProcessor;
import com.example.api.model.SortEmployeesRequest;
import com.example.api.model.SortEmployeesResponse;
import io.vavr.control.Either;

public interface SortEmployeesProcessor extends OperationProcessor<SortEmployeesRequest, SortEmployeesResponse> {
    Either<BaseError, SortEmployeesResponse> process(SortEmployeesRequest sortEmployeesRequest);
}
