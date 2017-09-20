package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 13/08/2017.
 */

public class rowV2Cholesterol {

    @Getter
    @Setter
    String identifier;
    @Getter
    @Setter
    float cholesterol;
    @Getter
    @Setter
    float triglycerides;
    @Getter
    @Setter
    float hdl;
    @Getter
    @Setter
    float ldl;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowV2Cholesterol(String identifier, float cholesterol, float triglycerides, float hdl, float ldl, String date, String observations) {
        this.identifier = identifier;
        this.cholesterol = cholesterol;
        this.triglycerides = triglycerides;
        this.ldl = hdl;
        this.ldl = ldl;
        this.date = date;
        this.observations = observations;
    }

}
