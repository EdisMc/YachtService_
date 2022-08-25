package com.example.data.client;

import com.example.api.model.FindYachtResponse;
import com.example.data.db.entity.Yacht;

import java.util.List;

public interface FindYachtsService {
    FindYachtResponse getYachts(List<Yacht> yachts);
}
