package com.example.api;

import com.example.api.base.OperationInput;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PaymentServiceRequest implements OperationInput {
    private String cardNumber;
    private double totalPriceForRent;
}