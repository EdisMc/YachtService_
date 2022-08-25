package com.example.core.processor;

import com.example.api.PaymentFeignInterface;
import com.example.api.PaymentServiceRequest;
import com.example.api.PaymentServiceResponse;
import com.example.api.error.PaymentError;
import com.example.api.error.PaymentServiceNotRespondingError;
import com.example.api.model.RentAYachtRequest;
import com.example.api.model.RentAYachtResponse;
import com.example.data.client.PriceService;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Employee;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.CustomerRepository;
import com.example.data.db.repository.EmployeeRepository;
import com.example.data.db.repository.YachtRentRepository;
import com.example.data.db.repository.YachtRepository;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;

class RentYachtProcessorCoreTest {

    @Mock
    private PaymentFeignInterface paymentFeignInterface;

    @Mock
    private YachtRepository yachtRepository;

    @Mock
    private PriceService priceService;

    @Mock
    private YachtRentRepository yachtRentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private RentYachtProcessorCore rentYachtProcessorCore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void rentTest() {

        Yacht yacht1 = Yacht
                .builder()
                .yachtId(1L)
                .number("DFS2342335DSS")
                .price(150.00)
                .status(false)
                .build();

        Customer customer1 = Customer
                .builder()
                .id(1L)
                .fullName("Ivan Ivanov")
                .customerStatus(false)
                .build();

        Employee employee1 = Employee
                .builder()
                .id(1L)
                .fullName("Pesho Peshov")
                .positionId(1L)
                .build();

        RentAYachtRequest rentAYachtRequest = RentAYachtRequest
                .builder()
                .yachtNumber(yacht1.getNumber())
                .cardNumber("123454534674657")
                .daysForRent(3)
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .build();

        Mockito.when(yachtRepository.findYachtByStatusAndNumber(false, yacht1.getNumber()))
                .thenReturn(Optional.of(yacht1));

        Mockito.when(yachtRepository.findYachtByNumber(yacht1.getNumber()))
                .thenReturn(Optional.of(yacht1));

        Mockito.when(customerRepository.getCustomerById(rentAYachtRequest.getCustomerId()))
                .thenReturn(Optional.of(customer1));

        Mockito.when(employeeRepository.getEmployeeById(rentAYachtRequest.getCustomerId()))
                .thenReturn(Optional.of(employee1));

        Mockito.when(priceService.getPrice(rentAYachtRequest.getDaysForRent(), yacht1.getPrice()))
                .thenReturn(100.0);

        YachtRent rent1 = YachtRent
                .builder()
                .id(1L)
                .yachtId(yacht1.getYachtId())
                .customerId(rentAYachtRequest.getCustomerId())
                .employeeId(rentAYachtRequest.getEmployeeId())
                .date(LocalDate.now())
                .daysForRent(rentAYachtRequest.getDaysForRent())
                .price(priceService.getPrice(rentAYachtRequest.getDaysForRent(), yacht1.getPrice()))
                .build();

        Mockito.when(yachtRentRepository.save(rent1)).thenReturn(null);

        Mockito.when(customerRepository.save(customer1)).thenReturn(null);

        Mockito.when(yachtRepository.save(yacht1)).thenReturn(null);


        PaymentServiceRequest paymentServiceRequest = PaymentServiceRequest
                .builder()
                .cardNumber(rentAYachtRequest.getCardNumber())
                .totalPriceForRent(priceService.getPrice(rentAYachtRequest.getDaysForRent(), yacht1.getPrice()))
                .build();

        PaymentServiceResponse feignResponse = PaymentServiceResponse
                .builder()
                .responseStatus(200)
                .message("Payment is successful!")
                .build();

        RentAYachtResponse rentAYachtResponse = RentAYachtResponse
                .builder()
                .result("Payment is successful!")
                .build();

        Mockito.when(paymentFeignInterface.pay(paymentServiceRequest))
                .thenReturn(feignResponse);

        Assertions.assertEquals(feignResponse, paymentFeignInterface.pay(paymentServiceRequest));
        Assertions.assertEquals(rentAYachtResponse, rentYachtProcessorCore.process(rentAYachtRequest).get());

        Mockito.when(paymentFeignInterface.pay(paymentServiceRequest))
                .thenThrow(FeignException.FeignClientException.class);

        PaymentError paymentServiceError = new PaymentError();
        Assertions.assertEquals(paymentServiceError, rentYachtProcessorCore.process(rentAYachtRequest).getLeft());
    }

    @Test
    public void feignTest() {
        Yacht yacht1 = Yacht
                .builder()
                .yachtId(1L)
                .number("566S4RS6225299")
                .price(200.00)
                .status(false)
                .build();

        Customer customer1 = Customer
                .builder()
                .id(1L)
                .fullName("Pesho Peshov")
                .customerStatus(false)
                .build();

        Employee employee1 = Employee
                .builder()
                .id(1L)
                .fullName("Pesho Ivanov")
                .positionId(1L)
                .build();

        RentAYachtRequest rentACarRequest = RentAYachtRequest
                .builder()
                .yachtNumber(yacht1.getNumber())
                .cardNumber("03223451234567")
                .daysForRent(5)
                .customerId(customer1.getId())
                .employeeId(employee1.getId())
                .build();

        PaymentServiceRequest paymentServiceRequest = PaymentServiceRequest
                .builder()
                .cardNumber(rentACarRequest.getCardNumber())
                .totalPriceForRent(priceService.getPrice(rentACarRequest.getDaysForRent(), yacht1.getPrice()))
                .build();

        Mockito.when(paymentFeignInterface.pay(paymentServiceRequest))
                .thenThrow(RetryableException.class);

        Mockito.when(yachtRepository.findYachtByStatusAndNumber(false, yacht1.getNumber()))
                .thenReturn(Optional.of(yacht1));

        PaymentServiceNotRespondingError paymentServiceNotRespondingError = new PaymentServiceNotRespondingError();
        Assertions.assertEquals(paymentServiceNotRespondingError, rentYachtProcessorCore.process(rentACarRequest).getLeft());

    }

}
