package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjUserdate {
    @Getter
    @Setter
    private String identifier ;
    @Getter
    @Setter
    private String startDate;
    @Getter
    @Setter
    private String endDate;

    public ObjUserdate(String identifier, String startDate, String endDate) {
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
