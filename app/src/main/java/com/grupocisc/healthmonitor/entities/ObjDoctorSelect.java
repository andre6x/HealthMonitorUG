package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjDoctorSelect {
    @Getter
    @Setter
    private String identifier ;
    @Getter
    @Setter
    private int doctorId;

    public ObjDoctorSelect(String identifier, int doctorId) {
        this.identifier = identifier;
        this.doctorId = doctorId;
    }
}
