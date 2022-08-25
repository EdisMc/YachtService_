package com.example.api.model;

import com.example.api.base.OperationInput;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RentAYachtRequest implements OperationInput {

    @Min(value = 1)
    private Integer daysForRent;

    @Min(1)
    private Long customerId;

    @Min(1)
    private Long employeeId;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String yachtNumber;
}
