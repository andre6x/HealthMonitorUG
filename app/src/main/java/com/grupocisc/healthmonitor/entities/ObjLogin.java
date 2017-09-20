package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 10/8/17.
 */

public class ObjLogin {
    @Getter
    @Setter
    private String identifier ;
    @Getter
    @Setter
    private String password;

    public ObjLogin(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

}
