package com.example.api.model;

import com.example.api.base.OperationResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@Builder
public class SortYachtResponse implements OperationResult {
    Map<String, Integer> yachtsSortedByRentingTime;
}
