package com.grupocisc.healthmonitor.entities;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 11/8/17.
 */

public class ObjtSavePickFlow {
    @Getter
    @Setter
    private String identifier ;
    @Getter
    @Setter
    private float measureUnits ;
    @Getter
    @Setter
    private String date;
    @Getter
    @Setter
    private String observations;
    @Getter
    @Setter
    private ArrayList<ArrayParamsFavoritesPick> listaSintomas;
    @Getter
    @Setter
    private ArrayList<ArrayParamsFavoritesPick> listaDesencadenantes;


    public ObjtSavePickFlow(String identifier, float measureUnits, String date, String observations, ArrayList<ArrayParamsFavoritesPick> listaSintomas, ArrayList<ArrayParamsFavoritesPick> listaDesencadenantes) {
        this.identifier = identifier;
        this.measureUnits = measureUnits;
        this.date = date;
        this.observations = observations;
        this.listaSintomas = listaSintomas;
        this.listaDesencadenantes = listaDesencadenantes;
    }
}
