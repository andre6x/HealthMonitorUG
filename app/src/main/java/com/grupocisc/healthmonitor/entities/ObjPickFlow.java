package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjPickFlow {
    @Getter
    @Setter
    private float peakFlow ;


    public ObjPickFlow(float peakFlow) {
        this.peakFlow = peakFlow;
    }
}
