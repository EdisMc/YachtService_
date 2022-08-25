package com.example.api.operation;

import com.example.api.base.BaseError;
import com.example.api.base.OperationProcessor;
import com.example.api.model.RentAYachtRequest;
import com.example.api.model.RentAYachtResponse;
import io.vavr.control.Either;

public interface RentYachtProcessor extends OperationProcessor<RentAYachtRequest, RentAYachtResponse> {
    Either<BaseError, RentAYachtResponse> process(RentAYachtRequest rentACarRequest);
}
