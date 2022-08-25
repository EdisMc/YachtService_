package com.example.api.error;

import com.example.api.base.BaseError;
import org.springframework.http.HttpStatus;

public class PaymentServiceNotRespondingError implements BaseError {

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }

    @Override
    public String getErrorMessage() {
        return "Payment service not responding!";
    }
}
