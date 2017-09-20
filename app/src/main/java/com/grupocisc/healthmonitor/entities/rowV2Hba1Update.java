package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 13/08/2017.
 */

public class rowV2Hba1Update {

    @Getter
    @Setter
    int examsDetailId;
    @Getter
    @Setter
    float hba1c;
    @Getter
    @Setter
    float cetonas;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowV2Hba1Update(int examsDetailId, float hba1c, float cetonas, String date, String observations) {
        this.examsDetailId = examsDetailId;
        this.hba1c = hba1c;
        this.cetonas = cetonas;
        this.date = date;
        this.observations = observations;
    }
}
