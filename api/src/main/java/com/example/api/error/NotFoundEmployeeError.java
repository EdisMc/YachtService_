package com.example.api.error;

import com.example.api.base.BaseError;
import org.springframework.http.HttpStatus;

public class NotFoundEmployeeError implements BaseError {

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorMessage() {
        return "The employee doesn't exist!";
    }
}
