package com.example.core.processor;

import com.example.api.model.ReturnYachtRequest;
import com.example.api.model.SortEmployeesRequest;
import com.example.api.model.SortEmployeesResponse;
import com.example.core.exception.NotFoundRents;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Employee;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.EmployeeRepository;
import com.example.data.db.repository.YachtRentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SortEmployeesProcessorCoreTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private YachtRentRepository yachtRentRepository;

    @InjectMocks
    private SortEmployeesProcessorCore sortEmployeesProcessorCore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sortTest() {
        Yacht yacht1 = Yacht
                .builder()
                .yachtId(1L)
                .number("2G4AG55M8RS622444")
                .price(45.0)
                .status(true)
                .build();

        Customer customer1 = Customer
                .builder()
                .id(1L)
                .fullName("Drago Ivanov")
                .customerStatus(true)
                .build();

        Employee employee1 = Employee
                .builder()
                .id(1L)
                .fullName("Petko Ivanov")
                .positionId(1L)
                .build();

        Employee employee2 = Employee
                .builder()
                .id(2L)
                .fullName("Ivan Ivanov")
                .positionId(2L)
                .build();

        Employee employee3 = Employee
                .builder()
                .id(3L)
                .fullName("Georgi Georgiev")
                .positionId(1L)
                .build();

        ReturnYachtRequest returnYachtRequest = ReturnYachtRequest
                .builder()
                .yachtId(yacht1.getYachtId())
                .build();

        YachtRent yachtRent1 = YachtRent
                .builder()
                .id(1L)
                .yachtId(yacht1.getYachtId())
                .yacht(yacht1)
                .customerId(customer1.getId())
                .customer(customer1)
                .employeeId(employee1.getId())
                .employee(employee1)
                .price(400.0)
                .daysForRent(7)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent2 = YachtRent
                .builder()
                .id(2L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .price(250.0)
                .daysForRent(5)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent3 = YachtRent
                .builder()
                .id(3L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .price(200.0)
                .daysForRent(4)
                .date(LocalDate.now())
                .build();

        Mockito.when(yachtRentRepository.findAll())
                .thenReturn(List.of(yachtRent1, yachtRent2, yachtRent3));

        Mockito.when(employeeRepository.findAll())
                .thenReturn(List.of(employee1, employee2, employee3));

        Mockito.when(yachtRentRepository.getYachtRentsByEmployeeId(employee1.getId()))
                .thenReturn(List.of(yachtRent1, yachtRent2, yachtRent3));

        SortEmployeesRequest sortEmployeesRequest = SortEmployeesRequest
                .builder()
                .build();

        Map<String, Integer> sortedEmployees = new HashMap<>();
        sortedEmployees.put(employee1.getFullName(), 3);
        sortedEmployees.put(employee2.getFullName(), 0);
        sortedEmployees.put(employee3.getFullName(), 0);

        SortEmployeesResponse sortEmployeesResponse = SortEmployeesResponse
                .builder()
                .sortedEmployeesByRentingTime(sortedEmployees)
                .build();

        Assertions.assertEquals(sortEmployeesResponse, sortEmployeesProcessorCore.process(sortEmployeesRequest).get());

    }

    @Test
    void exceptionTest() {
        SortEmployeesRequest sortEmployeesRequest = SortEmployeesRequest
                .builder()
                .build();

        NotFoundRents rentsNotFoundException = new NotFoundRents();
        NotFoundRents rentsNotFoundError = new NotFoundRents();

        Mockito.when(yachtRentRepository.findAll())
                .thenThrow(rentsNotFoundException);

        Assertions.assertEquals(rentsNotFoundError, sortEmployeesProcessorCore.process(sortEmployeesRequest).getLeft());
    }
}