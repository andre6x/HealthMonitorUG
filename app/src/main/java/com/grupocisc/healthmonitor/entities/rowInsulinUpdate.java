package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 04/08/2017.
 */

public class rowInsulinUpdate {

    @Getter
    @Setter
    int insulinDetailId;
    @Getter
    @Setter
    int measureUnits;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowInsulinUpdate(int insulinDetailId, int measureUnits, String date, String observations) {
        this.insulinDetailId = insulinDetailId;
        this.measureUnits = measureUnits;
        this.date = date;
        this.observations = observations;
    }
}
