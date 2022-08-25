package com.example.api.error;

import com.example.api.base.BaseError;
import org.springframework.http.HttpStatus;

public class UnavailableRentService implements BaseError {

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorMessage() {
        return "Rent service is unavailable!";
    }
}
