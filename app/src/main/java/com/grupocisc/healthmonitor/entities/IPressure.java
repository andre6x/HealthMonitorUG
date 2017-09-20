package com.grupocisc.healthmonitor.entities;


/**
 * Created by Mariuxi on 13/01/2017.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "PressureTable")
public class IPressure implements Serializable {
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String maxpressure;
    @DatabaseField
    private String minpressure;// se cambia concentracion por peso
    @DatabaseField
    private String pulso;
    @DatabaseField
    private String medido;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String hora;
    @DatabaseField
    private String observacion;
    @DatabaseField
    private String enviadoServer ;
    @DatabaseField
    private int idBdServer ;
    @DatabaseField
    private String operacion ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaxPressure() {
        return maxpressure;
    }//se cambia concentracion por peso

    public void setMaxPressure(String maxpressure) {//se cambia concentracion por peso
        this.maxpressure = maxpressure;
    }

    public String getMinPressure() {
        return minpressure;
    }//se cambia concentracion por peso

    public void setMinPressure(String minpressure) {//se cambia concentracion por peso
        this.minpressure = minpressure;
    }

    public String getPulso() {
        return pulso;
    }

    public void setPulso(String pulso) {
        this.pulso = pulso;
    }

    public String getMedido() {
        return medido;
    }

    public void setMedido(String medido) {
        this.medido = medido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEnviadoServer() {
        return enviadoServer;
    }

    public void setEnviadoServer(String enviadoServer) {
        this.enviadoServer = enviadoServer;
    }

    public int getIdBdServer() {
        return idBdServer;
    }

    public void setIdBdServer(int idBdServer) {
        this.idBdServer = idBdServer;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}

