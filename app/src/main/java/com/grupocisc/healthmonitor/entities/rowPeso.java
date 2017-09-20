package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andres on 31/7/17.
 */

public class rowPeso {
    @Getter
    @Setter
    String identifier ;
    @Getter
    @Setter
    float weight ;
    @Getter
    @Setter
    float imc;
    @Getter
    @Setter
    float tmb;
    @Getter
    @Setter
    float waterPercentage;
    @Getter
    @Setter
    float greasePercentage;
    @Getter
    @Setter
    float dmo;
    @Getter
    @Setter
    float muscleMass;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String observations;


    public rowPeso(String identifier, float weight, float imc, float tmb, float waterPercentage, float greasePercentage, float dmo, float muscleMass, String date, String observations) {
        this.identifier = identifier;
        this.weight = weight;
        this.imc = imc;
        this.tmb = tmb;
        this.waterPercentage = waterPercentage;
        this.greasePercentage = greasePercentage;
        this.dmo = dmo;
        this.muscleMass = muscleMass;
        this.date = date;
        this.observations = observations;
    }
}
