package com.example.api.base;

import org.springframework.http.HttpStatus;

public interface BaseError {
    HttpStatus getErrorCode();
    String getErrorMessage();
}
