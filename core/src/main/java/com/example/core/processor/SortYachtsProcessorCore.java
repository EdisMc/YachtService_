package com.example.core.processor;

import com.example.api.base.BaseError;
import com.example.api.error.NotFoundRent;
import com.example.api.error.UnavailableRentService;
import com.example.api.model.SortYachtResponse;
import com.example.api.model.SortYachtsRequest;
import com.example.api.operation.SortYachtsProcessor;
import com.example.core.exception.NotFoundRents;
import com.example.data.db.entity.Yacht;
import com.example.data.db.entity.YachtRent;
import com.example.data.db.repository.YachtRentRepository;
import com.example.data.db.repository.YachtRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SortYachtsProcessorCore implements SortYachtsProcessor {

    private final YachtRentRepository yachtRentRepository;
    private final YachtRepository yachtRepository;

    public SortYachtsProcessorCore(YachtRentRepository yachtRentRepository, YachtRepository yachtRepository) {
        this.yachtRentRepository = yachtRentRepository;
        this.yachtRepository = yachtRepository;
    }

    @Override
    public Either<BaseError, SortYachtResponse> process(SortYachtsRequest sortYachtsRequest) {
        return Try.of(() -> {
                    List<YachtRent> rents = yachtRentRepository.findAll();

                    if (rents.isEmpty()) {
                        throw new NotFoundRents();
                    }

                    return SortYachtResponse
                            .builder()
                            .yachtsSortedByRentingTime(
                                    yachtRepository
                                            .findAll()
                                            .stream()
                                            .collect(Collectors
                                                    .toMap(
                                                            Yacht::getNumber,
                                                            yacht -> {
                                                                List<YachtRent> yachtRents = yachtRentRepository
                                                                        .getYachtRentsByYachtId(yacht.getYachtId());

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
                    if (throwable instanceof NotFoundRents) {
                        return new NotFoundRent();
                    }
                    return new UnavailableRentService();
                });
    }
}

