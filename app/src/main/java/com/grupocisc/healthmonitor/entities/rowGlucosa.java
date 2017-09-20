package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 14/08/2017.
 */

public class rowGlucosa {

     @Getter
     @Setter
     String identifier;
     @Getter
     @Setter
     int measureUnits;
     @Getter
     @Setter
     String date;
     @Getter
     @Setter
     String observations;

    public rowGlucosa(String identifier, int measureUnits, String date, String observations) {
        this.identifier = identifier;
        this.measureUnits = measureUnits;
        this.date = date;
        this.observations = observations;
    }
}
