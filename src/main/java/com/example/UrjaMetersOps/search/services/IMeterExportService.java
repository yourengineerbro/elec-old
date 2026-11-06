package com.example.UrjaMetersOps.search.services;

import com.example.UrjaMetersOps.search.dtos.MeterExportResponseDto;

import java.util.List;


public interface IMeterExportService {
    List<MeterExportResponseDto> exportMeter();
}
