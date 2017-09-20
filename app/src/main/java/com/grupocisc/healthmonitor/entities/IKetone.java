package com.grupocisc.healthmonitor.entities;

/**
 * Created by RODRIGUEZ on 14/06/2017.
 */

import  com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
@DatabaseTable(tableName = "KetonesTable")
public class IKetone implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int concentracion;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String hora;
    @DatabaseField
    private String observacion;
    @DatabaseField
    private String enviadoServer;
    @DatabaseField
    private String idBdServer;
    @DatabaseField
    private String operacion;

    public String getIdBdServer() {
        return idBdServer;
    }

    public void setIdBdServer(String idBdServer) {
        this.idBdServer = idBdServer;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public IKetone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(int concentracion) {
        this.concentracion = concentracion;
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
}

