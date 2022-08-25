package com.example.api.model;

import com.example.api.base.OperationInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindYachtRequest implements OperationInput {
    private Boolean status;
}
