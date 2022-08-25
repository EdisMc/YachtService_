package com.example.core.processor;

import com.example.api.base.BaseError;
import com.example.api.error.UnavailableRentService;
import com.example.api.error.YachtNotTakenError;
import com.example.api.model.ReturnYachtRequest;
import com.example.api.model.ReturnYachtResponse;
import com.example.api.operation.ReturnYachtProcessor;
import com.example.core.exception.NotFoundRent;
import com.example.core.exception.YachtNotTakenException;
import com.example.data.db.entity.Customer;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.CustomerRepository;
import com.example.data.db.repository.YachtRentRepository;
import com.example.data.db.repository.YachtRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReturnYachtProcessorCore implements ReturnYachtProcessor {

    private final YachtRentRepository yachtRentRepository;
    private final CustomerRepository customerRepository;
    private final YachtRepository yachtRepository;

    public ReturnYachtProcessorCore(YachtRentRepository yachtRentRepository, CustomerRepository customerRepository, YachtRepository yachtRepository) {
        this.yachtRentRepository = yachtRentRepository;
        this.customerRepository = customerRepository;
        this.yachtRepository = yachtRepository;
    }

    @Override
    public Either<BaseError, ReturnYachtResponse> process(ReturnYachtRequest returnYachtRequest) {
        return Try.of(() -> {

                            List<YachtRent> yachtRents = yachtRentRepository
                                    .getYachtRentsByYachtId(
                                            returnYachtRequest.getYachtId()
                                    );

                            if(yachtRents.isEmpty()) {
                                throw new NotFoundRent();
                            }

                            YachtRent foundYachtRent = yachtRents
                                    .stream()
                                    .filter(yachtRent -> yachtRent.getYacht().getStatus().equals(true))
                                    .findFirst()
                                    .orElseThrow(YachtNotTakenException::new);

                            Yacht yachtToReturn = foundYachtRent.getYacht();

                            yachtToReturn.setStatus(false);
                            yachtRepository.save(yachtToReturn);

                            Customer customerToUpdate = foundYachtRent.getCustomer();
                            customerToUpdate.setCustomerStatus(false);
                            customerRepository.save(customerToUpdate);

                            return ReturnYachtResponse
                                    .builder()
                                    .result("The yacht was successfully returned!")
                                    .build();
                        }
                )
                .toEither()
                .mapLeft(throwable -> {
                    if(throwable instanceof NotFoundRent) {
                        return new com.example.api.error.NotFoundRent();
                    }
                    if(throwable instanceof YachtNotTakenException) {
                        return new YachtNotTakenError();
                    }
                    return new UnavailableRentService();
                });
    }
}

