package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 13/08/2017.
 */

public class rowV2CholesterolUpdate {

    @Getter
    @Setter
    int     cholesterolDetailId;
    @Getter
    @Setter
    float   cholesterol;
    @Getter
    @Setter
    float   triglycerides;
    @Getter
    @Setter
    float   hdl;
    @Getter
    @Setter
    float   ldl;
    @Getter
    @Setter
    String  date;
    @Getter
    @Setter
    String  observations;

    public rowV2CholesterolUpdate(int cholesterolDetailId, float cholesterol, float triglycerides, float ldl, float hdl, String date, String observations) {

        this.cholesterolDetailId = cholesterolDetailId;
        this.cholesterol = cholesterol;
        this.triglycerides = triglycerides;
        this.hdl = hdl;
        this.ldl = ldl;
        this.date = date;
        this.observations = observations;
    }

}
