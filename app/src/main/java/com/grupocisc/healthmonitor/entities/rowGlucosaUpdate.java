package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 14/08/2017.
 */

public class rowGlucosaUpdate {

    @Getter
    @Setter
    int glucoseDetailId;
    @Getter
    @Setter
    int measureUnits;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;


    public rowGlucosaUpdate(int glucoseDetailId, int measureUnits, String date, String observations) {
        this.glucoseDetailId = glucoseDetailId;
        this.measureUnits = measureUnits;
        this.date = date;
        this.observations = observations;
    }
}
