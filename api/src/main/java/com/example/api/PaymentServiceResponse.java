package com.example.api;

import com.example.api.base.OperationResult;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentServiceResponse implements OperationResult {
    private Integer responseStatus;
    private String message;
}