package com.example.api.model;

import com.example.api.base.OperationResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class FindYachtResponse<YachtPOJO> implements OperationResult {
    List<YachtPOJO> yachtsAvailable;
}
