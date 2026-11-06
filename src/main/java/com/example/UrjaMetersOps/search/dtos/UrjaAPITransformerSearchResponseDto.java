package com.example.UrjaMetersOps.search.dtos;

import lombok.Data;
import java.util.List;

@Data
public class UrjaAPITransformerSearchResponseDto {
    private List<TransformerResponseDto> data;
}
