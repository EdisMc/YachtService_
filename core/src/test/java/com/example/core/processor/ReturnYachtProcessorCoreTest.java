package com.example.core.processor;

import com.example.api.model.ReturnYachtRequest;
import com.example.api.model.ReturnYachtResponse;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Employee;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.CustomerRepository;
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
import java.util.List;

public class ReturnYachtProcessorCoreTest {

    @Mock
    private YachtRentRepository yachtRentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private YachtRepository yachtRepository;

    @InjectMocks
    private ReturnYachtProcessorCore returnYachtProcessorCore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnYachtTest() {
        Yacht yacht1 = Yacht
                .builder()
                .yachtId(1L)
                .number("3G4AG55M8RS622999")
                .price(50.0)
                .status(true)
                .build();

        Customer customer1 = Customer
                .builder()
                .id(1L)
                .fullName("Pesho Ivanov")
                .customerStatus(true)
                .build();

        Employee employee1 = Employee
                .builder()
                .id(1L)
                .fullName("Ivan Ivanov")
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
                .price(300.0)
                .daysForRent(5)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent2 = YachtRent
                .builder()
                .id(2L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .price(300.0)
                .daysForRent(6)
                .date(LocalDate.now())
                .build();

        YachtRent yachtRent3 = YachtRent
                .builder()
                .id(3L)
                .yachtId(yacht1.getYachtId())
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .price(250.0)
                .daysForRent(5)
                .date(LocalDate.now())
                .build();

        ReturnYachtResponse returnYachtResponse = ReturnYachtResponse
                .builder()
                .result("The car is successfully returned!")
                .build();

        Mockito.when(yachtRentRepository.getYachtRentsByYachtId(returnYachtRequest.getYachtId()))
                .thenReturn(List.of(yachtRent1, yachtRent2, yachtRent3));

        Mockito.when(yachtRentRepository.save(yachtRent1))
                .thenReturn(null);

        Mockito.when(customerRepository.save(customer1))
                .thenReturn(null);

        Assertions.assertEquals(returnYachtResponse, returnYachtProcessorCore.process(returnYachtRequest).get());

    }
}
