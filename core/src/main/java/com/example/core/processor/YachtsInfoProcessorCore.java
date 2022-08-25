package com.example.core.processor;

import com.example.api.base.BaseError;
import com.example.api.error.ApiError;
import com.example.api.error.ApiServiceNotRespondingError;
import com.example.api.error.UnavailableRentService;
import com.example.api.error.YachtNotFoundError;
import com.example.api.model.FindYachtRequest;
import com.example.api.model.FindYachtResponse;
import com.example.api.operation.GetYachtsInfoProcessor;
import com.example.core.exception.YachtNotFoundException;
import com.example.data.client.FindYachtsService;
import com.example.data.db.entity.Yacht;
import com.example.data.db.repository.YachtRepository;
import feign.FeignException;
import feign.RetryableException;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YachtsInfoProcessorCore implements GetYachtsInfoProcessor {

    private final FindYachtsService findYachtService;
    private final YachtRepository yachtRepository;

    public YachtsInfoProcessorCore(FindYachtsService findYachtService, YachtRepository yachtRepository) {
        this.findYachtService = findYachtService;
        this.yachtRepository = yachtRepository;
    }

    @Override
    public Either<BaseError, FindYachtResponse> process(FindYachtRequest findYachtRequest) {
        return Try.of(() -> {
                    List<Yacht> yachts = yachtRepository.findAllByStatus(findYachtRequest.getStatus());

                    if (yachts.isEmpty()) {
                        throw new YachtNotFoundException();
                    }

                    return FindYachtResponse
                            .builder()
                            .yachtsAvailable(findYachtService.getYachts(yachts).getYachtsAvailable())
                            .build();

                })
                .toEither().mapLeft(throwable -> {
                    if (throwable instanceof YachtNotFoundException) {
                        return new YachtNotFoundError();
                    }
                    if (throwable instanceof RetryableException) {
                        return new ApiServiceNotRespondingError();
                    }
                    if (throwable instanceof FeignException.FeignClientException) {
                        return new ApiError();
                    }
                    return new UnavailableRentService();
                });
    }
}
