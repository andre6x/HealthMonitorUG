package com.grupocisc.healthmonitor.entities;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Gema on 10/01/2017.
 */
//@DatabaseTable (tableName = "DiseaseTable")
@DatabaseTable (tableName = Constantes.TABLA_DISEASE)

public class IDisease {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String disease;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String hora;
    @DatabaseField
    private String observacion;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


    public IDisease(){
    }


}
