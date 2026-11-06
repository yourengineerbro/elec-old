package com.example.UrjaMetersOps.search.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UrjaAPIMeterSearchResponseDto {

    private List<MeterSearchResponseDto> data;

//  "meterId":"J100000","serialNo":"SE33962",
//          "make":"HPL","phaseType":"single","installStatus":"Decommissioned",
//          "dtCode":"DT-001"
}
