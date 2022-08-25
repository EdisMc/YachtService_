package com.example.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class SortEmployeesResponse {
    Map<String, Integer> sortedEmployeesByRentingTime;
}
