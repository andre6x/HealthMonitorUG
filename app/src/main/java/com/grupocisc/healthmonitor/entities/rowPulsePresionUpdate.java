package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 10/08/2017.
 */

public class rowPulsePresionUpdate {

    @Getter
    @Setter
    int pressureDetailId;
    @Getter
    @Setter
    float diastolicPressure  ;
    @Getter
    @Setter
    float systolicPressure;
    @Getter
    @Setter
    String activity;
    @Getter
    @Setter
    float pulse;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowPulsePresionUpdate(int pressureDetailId, float diastolicPressure, float systolicPressure,  String activity,float pulse, String date, String observations) {
        this.pressureDetailId= pressureDetailId;
        this.diastolicPressure = diastolicPressure;
        this.systolicPressure = systolicPressure;
        this.pulse = pulse;
        this.activity = activity;
        this.date = date;
        this.observations = observations;
    }


}
