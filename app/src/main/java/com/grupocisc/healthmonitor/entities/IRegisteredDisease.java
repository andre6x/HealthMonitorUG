package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Gema on 26/01/2017.
 */
@DatabaseTable(tableName = "RDiseaseTable")

public class IRegisteredDisease {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String nombre;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String presentacion;
    @DatabaseField
    private String via;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {//se cambia concentracion por peso
        this.nombre = nombre;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via= via;
    }

}
