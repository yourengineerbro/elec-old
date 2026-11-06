package com.example.UrjaMetersOps.search.services;

import com.example.UrjaMetersOps.search.dtos.TransformerResponseDto;

import java.util.List;

public interface ITransformerSearchService {
    List<TransformerResponseDto> listTransformers(int page);
}
