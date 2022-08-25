package com.example.core.processor;

import com.example.api.error.RentsNotFound;
import com.example.api.model.SortYachtResponse;
import com.example.api.model.SortYachtsRequest;
import com.example.core.exception.NotFoundRents;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Employee;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.YachtRentRepository;
import com.example.data.db.repository.YachtRepository;
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

public class SortYachtsProcessorCoreTest {

    @Mock
    private YachtRentRepository yachtRentRepository;

    @Mock
    private YachtRepository yachtRepository;

    @InjectMocks
    private SortYachtsProcessorCore sortYachtsProcessorCore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setSortTest() {
        Yacht yacht1 = Yacht
                .builder()
                .yachtId(1L)
                .number("3G4AG55M8RS622007")
                .price(55.0)
                .status(true)
                .build();

        Yacht yacht2 = Yacht
                .builder()
                .yachtId(2L)
                .number("3G4AG55M8RS622544")
                .price(45.0)
                .status(true)
                .build();

        Yacht yacht3 = Yacht
                .builder()
                .yachtId(3L)
                .number("3G4AG55M8RS62256734")
                .price(75.0)
                .status(true)
                .build();

        Customer customer1 = Customer
                .builder()
                .id(1L)
                .fullName("Petko Petkov")
                .customerStatus(true)
                .build();

        Employee employee1 = Employee
                .builder()
                .id(1L)
                .fullName("Gosho Ivanov")
                .positionId(1L)
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
                .price(300.0)
                .daysForRent(5)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent2 = YachtRent
                .builder()
                .id(2L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .customer(customer1)
                .employeeId(employee1.getId())
                .employee(employee1)
                .price(250.0)
                .daysForRent(5)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent3 = YachtRent
                .builder()
                .id(3L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .customer(customer1)
                .employeeId(employee1.getId())
                .employee(employee1)
                .price(150.0)
                .daysForRent(3)
                .date(LocalDate.now())
                .build();

        Mockito.when(yachtRentRepository.findAll())
                .thenReturn(List.of(yachtRent1, yachtRent3, yachtRent3));

        Mockito.when(yachtRentRepository.getYachtRentsByYachtId(yacht1.getYachtId()))
                .thenReturn(List.of(yachtRent1, yachtRent2, yachtRent3));

        Mockito.when(yachtRepository.findAll())
                .thenReturn(List.of(yacht1, yacht2, yacht3));

        SortYachtsRequest sortYachtsRequest = SortYachtsRequest
                .builder()
                .build();

        Map<String, Integer> sortedYachts = new HashMap<>();
        sortedYachts.put(yacht1.getNumber(), 3);
        sortedYachts.put(yacht2.getNumber(), 0);
        sortedYachts.put(yacht3.getNumber(), 0);

        SortYachtResponse sortYachtsResponse = SortYachtResponse
                .builder()
                .yachtsSortedByRentingTime(sortedYachts)
                .build();

        Assertions.assertEquals(sortYachtsResponse, sortYachtsProcessorCore.process(sortYachtsRequest).get());
    }

    @Test
    void exceptionTest() {
        SortYachtsRequest sortYachtsRequest = SortYachtsRequest
                .builder()
                .build();

        NotFoundRents rentsNotFoundException = new NotFoundRents();
        RentsNotFound rentsNotFoundError = new RentsNotFound();

        Mockito.when(yachtRentRepository.findAll())
                .thenThrow(rentsNotFoundException);

        Assertions.assertEquals(rentsNotFoundError, sortYachtsProcessorCore.process(sortYachtsRequest).getLeft());
    }
}
