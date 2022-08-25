package com.example.core.processor;

import com.example.api.base.BaseError;
import com.example.api.error.NotFoundRent;
import com.example.api.error.UnavailableRentService;
import com.example.api.model.SortEmployeesRequest;
import com.example.api.model.SortEmployeesResponse;
import com.example.api.operation.SortEmployeesProcessor;
import com.example.core.exception.NotFoundRents;
import com.example.data.db.entity.Employee;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.EmployeeRepository;
import com.example.data.db.repository.YachtRentRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SortEmployeesProcessorCore implements SortEmployeesProcessor {
    private final EmployeeRepository employeeRepository;
    private final YachtRentRepository yachtRentRepository;

    public SortEmployeesProcessorCore(EmployeeRepository employeeRepository, YachtRentRepository yachtRentRepository) {
        this.employeeRepository = employeeRepository;
        this.yachtRentRepository = yachtRentRepository;
    }

    @Override
    public Either<BaseError, SortEmployeesResponse> process(SortEmployeesRequest sortEmployeesRequest) {
        return Try.of(() -> {
                    List<YachtRent> rents = yachtRentRepository.findAll();

                    if (rents.isEmpty()) {
                        throw new NotFoundRents();
                    }

                    return SortEmployeesResponse
                            .builder()
                            .sortedEmployeesByRentingTime(
                                    employeeRepository
                                            .findAll()
                                            .stream()
                                            .collect(Collectors
                                                    .toMap(
                                                            Employee::getFullName,
                                                            employee -> {
                                                                List<YachtRent> yachtRents = yachtRentRepository
                                                                        .getYachtRentsByEmployeeId(employee.getId());

                                                                return yachtRents.size();
                                                            }
                                                    )
                                            )
                                            .entrySet()
                                            .stream()
                                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                            .collect(Collectors.toMap(
                                                            Map.Entry::getKey, Map.Entry::getValue,
                                                            (e1, e2) -> e1, LinkedHashMap::new
                                                    )
                                            )
                            )
                            .build();

                })
                .toEither()
                .mapLeft(throwable -> {
                    if(throwable instanceof NotFoundRents) {
                        return new NotFoundRent();
                    }
                    return new UnavailableRentService();
                });
    }
}
