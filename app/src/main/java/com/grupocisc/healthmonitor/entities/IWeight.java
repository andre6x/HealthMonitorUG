package com.grupocisc.healthmonitor.entities;

/**
 * Created by Mariuxi on 12/01/2017.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "WeightTable")
public class IWeight implements Serializable {
    @Getter
    @Setter
    @DatabaseField(generatedId=true)
    private int id;
    @Getter
    @Setter
    @DatabaseField
    private int idBdServer ;
    @Getter
    @Setter
    @DatabaseField
    private float peso;// se cambia concentracion por peso
    @Getter
    @Setter
    @DatabaseField
    private float masamuscular;
    @Getter
    @Setter
    @DatabaseField
    private float tmb;
    @Getter
    @Setter
    @DatabaseField
    private float dmo;
    @Getter
    @Setter
    @DatabaseField
    private float porcentajeAgua;
    @Getter
    @Setter
    @DatabaseField
    private float porcentajeGrasa;
    @Getter
    @Setter
    @DatabaseField
    private String fecha;
    @Getter
    @Setter
    @DatabaseField
    private String hora;
    @Getter
    @Setter
    @DatabaseField
    private String observacion;
    @Getter
    @Setter
    @DatabaseField
    private String enviadoServer ;
    @Getter
    @Setter
    @DatabaseField
    private String operacion ;

}

