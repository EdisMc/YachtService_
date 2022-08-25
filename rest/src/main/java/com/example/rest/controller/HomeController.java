package com.example.rest.controller;

import com.example.api.base.BaseError;
import com.example.api.model.*;
import com.example.api.operation.*;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class HomeController {

    private final GetYachtsInfoProcessor getYachtsInfoProcessor;
    private final RentYachtProcessor rentYachtProcessor;
    private final ReturnYachtProcessor returnYachtProcessor;
    private final SortYachtsProcessor sortYachtsProcessor;
    private final SortEmployeesProcessor sortEmployeesProcessor;

    public HomeController(GetYachtsInfoProcessor getYachtsInfoProcessor, RentYachtProcessor rentYachtProcessor, ReturnYachtProcessor returnYachtProcessor, SortYachtsProcessor sortYachtsProcessor, SortEmployeesProcessor sortEmployeesProcessor) {
        this.getYachtsInfoProcessor = getYachtsInfoProcessor;
        this.rentYachtProcessor = rentYachtProcessor;
        this.returnYachtProcessor = returnYachtProcessor;
        this.sortYachtsProcessor = sortYachtsProcessor;
        this.sortEmployeesProcessor = sortEmployeesProcessor;
    }


    @PostMapping("/getYachts")
    public ResponseEntity<?> getYachtsByStatus(@RequestBody FindYachtRequest findYachtRequest) {
        Either<BaseError, FindYachtResponse> result = getYachtsInfoProcessor.process(findYachtRequest);

        if (result.isLeft()) {
            return ResponseEntity
                    .status(result.getLeft().getErrorCode())
                    .body(result.getLeft().getErrorMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }

    @PostMapping("/rentYacht")
    public ResponseEntity<?> rentAYacht(@Valid @RequestBody RentAYachtRequest rentAYachtRequest) {
        Either<BaseError, RentAYachtResponse> result = rentYachtProcessor.process(rentAYachtRequest);

        if (result.isLeft()) {
            return ResponseEntity
                    .status(result.getLeft().getErrorCode())
                    .body(result.getLeft().getErrorMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }

    @PostMapping("/returnYacht")
    public ResponseEntity<?> returnYacht(@Valid @RequestBody ReturnYachtRequest returnYachtRequest) {
        Either<BaseError, ReturnYachtResponse> result = returnYachtProcessor.process(returnYachtRequest);

        if (result.isLeft()) {
            return ResponseEntity
                    .status(result.getLeft().getErrorCode())
                    .body(result.getLeft().getErrorMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }

    @PostMapping("/sortYachtsByTimesRented")
    public ResponseEntity<?> sortYachtsByTimesRented(@RequestBody SortYachtsRequest sortYachtsRequest) {
        Either<BaseError, SortYachtResponse> result = sortYachtsProcessor.process(sortYachtsRequest);

        if (result.isLeft()) {
            return ResponseEntity
                    .status(result.getLeft().getErrorCode())
                    .body(result.getLeft().getErrorMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }

    @PostMapping("/sortEmployeesByTimesRented")
    public ResponseEntity<?> sortEmployeesByTimesRented(@RequestBody SortEmployeesRequest sortEmployeesRequest) {
        Either<BaseError, SortEmployeesResponse> result = sortEmployeesProcessor.process(sortEmployeesRequest);
        if (result.isLeft()) {
            return ResponseEntity
                    .status(result.getLeft().getErrorCode())
                    .body(result.getLeft().getErrorMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }
}

