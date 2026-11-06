package com.example.UrjaMetersOps.search.controllers;

import com.example.UrjaMetersOps.search.dtos.MeterSearchResponseDto;
import com.example.UrjaMetersOps.search.dtos.TransformerResponseDto;
import com.example.UrjaMetersOps.search.services.ITransformerSearchService;
import com.example.UrjaMetersOps.search.services.IMeterSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/search")
public class UtilitySearchController {

    @Autowired
    private IMeterSearchService IMeterSearchService;

    @Autowired
    private ITransformerSearchService iTransformerSearchService;



    @GetMapping("/meters")
    public ResponseEntity<List<MeterSearchResponseDto>> listMeters
            (@RequestParam(value = "q", required = false) String query, @RequestParam("page") int pageNumber) {

        List<MeterSearchResponseDto> responseDtos = IMeterSearchService.getMeters(query, pageNumber);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }


    @GetMapping("/transformers")
    public ResponseEntity<List<TransformerResponseDto>> listTransformers
            (@RequestParam("page") int pageNumber) {

        List<TransformerResponseDto> responseDtos = iTransformerSearchService.listTransformers(pageNumber);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }

}
