package com.example.data.client.impl;

import com.example.data.client.PriceService;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

    @Override
    public Double getPrice(Integer duration, Double price) {
        return duration * price;
    }
}
