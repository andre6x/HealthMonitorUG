package com.grupocisc.healthmonitor.entities;


import lombok.Getter;
import lombok.Setter;

public class rowV2Fit {

    @Getter
    @Setter
    String identifier;
    @Getter
    @Setter
    String startDate;
    @Getter
    @Setter
    String endDate;
    @Getter
    @Setter
    String activity;
    @Getter
    @Setter
    int value;

    public rowV2Fit(String identifier, String startD, String endD, String act, int val) {
        this.identifier = identifier;
        this.startDate = startD;
        this.endDate = endD;
        this.activity = act;
        this.value = val;

    }


}
