package com.example.api.model;

import com.example.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ReturnYachtRequest implements OperationInput {
    private Long yachtId;


}
