package com.example.UrjaMetersOps.search.services;

import com.example.UrjaMetersOps.search.dtos.MeterSearchResponseDto;

import java.util.List;

public interface IMeterSearchService {
    List<MeterSearchResponseDto> getMeters(String query, Integer page);
}
