package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jesenia on 06/08/2017.
 */

public class rowV2StateUpdate {

    @Getter
    @Setter
    int moodId ;
    @Getter
    @Setter
    int mood  ;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;

    public rowV2StateUpdate(int moodId, int mood, String date, String observations) {
        this.moodId = moodId;
        this.mood = mood;
        this.date = date;
        this.observations = observations;
    }
}


