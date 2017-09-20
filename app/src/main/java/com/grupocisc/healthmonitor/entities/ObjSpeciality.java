package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjSpeciality {
    @Getter
    @Setter
    private int specialtyId ;

    public ObjSpeciality(int specialtyId) {
        this.specialtyId = specialtyId;
    }
}
