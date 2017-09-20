package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 04/08/2017.
 */

public class rowInsulin {

    @Getter
    @Setter
    String identifier ;
    @Getter
    @Setter
    int measureUnits  ;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowInsulin(String identifier, int measureUnits, String date, String observations) {
        this.identifier = identifier;
        this.measureUnits = measureUnits;
        this.date = date;
        this.observations = observations;
    }
}
