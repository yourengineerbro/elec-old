package com.example.UrjaMetersOps.search.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({            // TODO: Find a clean way for this which is in this file only as of now.
        "meterId",
        "serialNo",
        "make",
        "phaseType",
        "installStatus",
        "installType",
        "build",
        "dtCode",
        "hierarchy",
        "geo"
})
public class MeterExportResponseDto {

    private String meterId;
    private String serialNo;
    private String make;
    private String phaseType;
    private String installStatus;
    private String installType;
    private String build;
    private String dtCode;   // TODO: Establish relationship here. to transformer
    private Hierarchy hierarchy;
    private MeterCoordinates geo;

    @JsonPropertyOrder({
            "zone",
            "circle",
            "division",
            "subDivision",
            "subStation",
            "feeder",
            "dt"
    })
    @Data
    private static class Hierarchy {
        private SubHierarchy zone;
        private SubHierarchy circle;
        private SubHierarchy division;
        private SubHierarchy subDivision;
        private SubHierarchy subStation;
        private SubHierarchy feeder;
        private SubHierarchy dt;    //   TODO: Establish relationship between meter and transformer via this.
    }

    @JsonPropertyOrder({
            "name",
            "code"
    })
    @Data
    public static class SubHierarchy {
        private String name;
        private String code;
    }

    @JsonPropertyOrder({
            "lat",
            "lng"
    })
    @Data
    public static class MeterCoordinates {
        private String lat;
        private String lng;
    }

}
