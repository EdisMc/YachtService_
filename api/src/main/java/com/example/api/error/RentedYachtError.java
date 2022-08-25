package com.example.api.error;

import com.example.api.base.BaseError;
import org.springframework.http.HttpStatus;

public class RentedYachtError implements BaseError {

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorMessage() {
        return "The customer has already rented a yacht!";
    }
}
