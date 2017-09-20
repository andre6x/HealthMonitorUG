package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjUser {
    @Getter
    @Setter
    private String identifier ;

    public ObjUser(String identifier) {
        this.identifier = identifier;
    }

}
