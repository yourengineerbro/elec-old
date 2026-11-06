package com.example.UrjaMetersOps.search.controllers;


import com.example.UrjaMetersOps.search.dtos.MeterExportResponseDto;
import com.example.UrjaMetersOps.search.dtos.MeterSearchResponseDto;
import com.example.UrjaMetersOps.search.services.IMeterExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/export")
public class UtilityExportController {


    @Autowired
    private IMeterExportService iMeterExportService;

    @GetMapping("/meters")
    public ResponseEntity<List<MeterExportResponseDto>> exportMeters(){

        List<MeterExportResponseDto> responseDto = new ArrayList<>();
        try {
            responseDto =  iMeterExportService.exportMeter();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
