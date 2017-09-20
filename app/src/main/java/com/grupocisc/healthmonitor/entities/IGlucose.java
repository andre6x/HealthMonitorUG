package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Date;

@DatabaseTable(tableName = "GlucoseTable")
public class IGlucose implements Serializable{
	
	@DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int idBdServer;
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
    private String operacion;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBdServer() {
        return idBdServer;
    }

    public void setIdBdServer(int idBdServer) {
        this.idBdServer = idBdServer;
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


    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

}
