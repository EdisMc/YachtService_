package com.example.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fakePayment", url = "http://localhost:8083")
public interface PaymentFeignInterface {

    @RequestMapping(method = RequestMethod.POST, value = "/pay")
    PaymentServiceResponse pay(@RequestBody PaymentServiceRequest paymentServiceRequest);
}