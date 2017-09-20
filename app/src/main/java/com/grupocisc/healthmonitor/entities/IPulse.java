package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Raymond on 13/01/2017.
 */

@DatabaseTable(tableName = "PulseTable")
public class IPulse  implements Serializable {

    @Getter
    @Setter
    @DatabaseField(generatedId=true)
    private int id;
    @Getter
    @Setter
    @DatabaseField
    private int idBdServer;
    @Getter
    @Setter
    @DatabaseField
    private int concentracion;
    @Getter
    @Setter
    @DatabaseField
    private String maxPressure;//SISTOLICA
    @Getter
    @Setter
    @DatabaseField
    private String minPressure;//DISTOLICA
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
    private String medido;
    @Getter
    @Setter
    @DatabaseField
    private String observacion;
    @Getter
    @Setter
    @DatabaseField
    private String enviadoServer;
    @Getter
    @Setter
    @DatabaseField
    private String operacion;


}
