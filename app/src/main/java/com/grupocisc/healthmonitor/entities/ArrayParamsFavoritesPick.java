package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by GrupoLink on 28/04/2015.
 */
public class ArrayParamsFavoritesPick {

    @Getter
    @Setter
    private int key;
    @Getter
    @Setter
    private int value;


    public ArrayParamsFavoritesPick(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
