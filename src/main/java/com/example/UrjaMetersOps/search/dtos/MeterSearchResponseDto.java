package com.example.UrjaMetersOps.search.dtos;

import lombok.Data;

@Data
public class MeterSearchResponseDto {
    private String meterId;
    private String serialNo;
    private String make;
    private String phaseType;
    private String installStatus;
    private String dtCode;

//     "meterId": "J100000",
//             "serialNo": "SE33962",
//             "make": "HPL",
//             "phaseType": "single",
//             "installStatus": "Decommissioned",
//             "dtCode": "DT-001"
}
