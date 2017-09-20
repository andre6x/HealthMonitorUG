package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 06/08/2017.
 */

public class rowV2State {

    @Getter
    @Setter
    String identifier ;
    @Getter
    @Setter
    int mood  ;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowV2State(String identifier, int mood, String date, String observations) {
        this.identifier = identifier;
        this.mood = mood;
        this.date = date;
        this.observations = observations;
    }
}


