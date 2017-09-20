package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 10/08/2017.
 */

public class rowPulsePresion {

    @Getter
    @Setter
    String identifier;
    @Getter
    @Setter
    float diastolicPressure;
    @Getter
    @Setter
    float systolicPressure;
    @Getter
    @Setter
    String activity;
    @Getter
    @Setter
    int pulse;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowPulsePresion(String identifier, float diastolicPressure, float systolicPressure, String activity, int pulse, String date, String observations) {
        this.identifier = identifier;
        this.diastolicPressure = diastolicPressure;
        this.systolicPressure = systolicPressure;
        this.activity = activity;
        this.pulse = pulse;
        this.date = date;
        this.observations = observations;
    }

}
