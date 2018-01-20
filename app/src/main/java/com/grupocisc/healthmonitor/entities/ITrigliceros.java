package com.grupocisc.healthmonitor.entities;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Adrian on 12/06/2017.
 */

//@DatabaseTable(tableName = "TriglicerosTable")
@DatabaseTable(tableName = Constantes.TABLA_TRIGLICERIDOS)

public class ITrigliceros implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int concentration;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcentration() {
        return concentration;
    }

    public void setConcentration(int concentration) {
        this.concentration = concentration;
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

    public ITrigliceros() {
    }



}
