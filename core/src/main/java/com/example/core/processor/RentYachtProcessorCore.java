package com.example.core.processor;

import com.example.api.PaymentFeignInterface;
import com.example.api.PaymentServiceRequest;
import com.example.api.PaymentServiceResponse;
import com.example.api.base.BaseError;
import com.example.api.error.*;
import com.example.api.model.RentAYachtRequest;
import com.example.api.model.RentAYachtResponse;
import com.example.api.operation.RentYachtProcessor;
import com.example.core.exception.CustomerRentedException;
import com.example.core.exception.NotFoundCustomerException;
import com.example.core.exception.NotFoundEmployee;
import com.example.core.exception.YachtNotFoundException;
import com.example.data.client.PriceService;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.CustomerRepository;
import com.example.data.db.repository.EmployeeRepository;
import com.example.data.db.repository.YachtRentRepository;
import com.example.data.db.repository.YachtRepository;
import feign.FeignException;
import feign.RetryableException;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class RentYachtProcessorCore implements RentYachtProcessor {

    private final PaymentFeignInterface paymentFeignInterface;
    private final YachtRepository yachtRepository;
    private final PriceService priceService;
    private final YachtRentRepository yachtRentRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public RentYachtProcessorCore(PaymentFeignInterface paymentFeignInterface, YachtRepository yachtRepository, PriceService priceService, YachtRentRepository yachtRentRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.paymentFeignInterface = paymentFeignInterface;
        this.yachtRepository = yachtRepository;
        this.priceService = priceService;
        this.yachtRentRepository = yachtRentRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public Either<BaseError, RentAYachtResponse> process(RentAYachtRequest rentAYachtRequest) {
        return Try.of(() -> {
                    double totalRentPrice = priceService
                            .getPrice(rentAYachtRequest.getDaysForRent(),
                                    yachtRepository
                                            .findYachtByStatusAndNumber(false,
                                                    rentAYachtRequest.getYachtNumber())
                                            .orElseThrow(YachtNotFoundException::new)
                                            .getPrice()

                            );

                    PaymentServiceResponse paymentServiceResponse = paymentFeignInterface
                            .pay(PaymentServiceRequest
                                    .builder()
                                    .cardNumber(rentAYachtRequest.getCardNumber())
                                    .totalPriceForRent(totalRentPrice)
                                    .build()
                            );

                    if (customerRepository
                            .getCustomerById(rentAYachtRequest.getCustomerId())
                            .orElseThrow(NotFoundCustomerException::new)
                            .getCustomerStatus()) {
                        throw new CustomerRentedException();
                    }

                    if (paymentServiceResponse.getResponseStatus().equals(200)) {

                        yachtRentRepository
                                .save(YachtRent
                                        .builder()
                                        .yachtId(yachtRepository
                                                .findYachtByNumber(rentAYachtRequest.getYachtNumber())
                                                .orElseThrow(YachtNotFoundException::new)
                                                .getYachtId()
                                        )
                                        .customerId(customerRepository
                                                .getCustomerById(rentAYachtRequest.getCustomerId())
                                                .orElseThrow(NotFoundCustomerException::new)
                                                .getId()
                                        )
                                        .employeeId(employeeRepository
                                                .getEmployeeById(rentAYachtRequest.getEmployeeId())
                                                .orElseThrow(NotFoundEmployee::new)
                                                .getId()
                                        )
                                        .date(LocalDate.now())
                                        .daysForRent(rentAYachtRequest.getDaysForRent())
                                        .price(totalRentPrice)
                                        .build()
                                );

                        Yacht yachtForUpdate = yachtRepository
                                .findYachtByNumber(rentAYachtRequest.getYachtNumber())
                                .orElseThrow(YachtNotFoundException::new);
                        yachtForUpdate.setStatus(true);
                        yachtRepository
                                .save(yachtForUpdate);

                        Customer customerToUpdate = customerRepository
                                .getCustomerById(rentAYachtRequest.getCustomerId())
                                .orElseThrow(NotFoundCustomerException::new);
                        customerToUpdate.setCustomerStatus(true);
                        customerRepository
                                .save(customerToUpdate);

                    }

                    return RentAYachtResponse
                            .builder()
                            .result(paymentServiceResponse.getMessage())
                            .build();
                })
                .toEither()
                .mapLeft(throwable -> {

                    if (throwable instanceof RetryableException) {
                        return new PaymentServiceNotRespondingError();
                    }
                    if (throwable instanceof YachtNotFoundException) {
                        return new YachtNotFoundError();
                    }
                    if (throwable instanceof NotFoundEmployee) {
                        return new NotFoundEmployeeError();
                    }
                    if (throwable instanceof NotFoundCustomerException) {
                        return new NotFoundCustomerError();
                    }
                    if (throwable instanceof CustomerRentedException) {
                        return new CustomerRentedError();
                    }
                    if (throwable instanceof FeignException.FeignClientException) {
                        return new PaymentError();
                    }

                    return new UnavailableRentService();
                });
    }

}
