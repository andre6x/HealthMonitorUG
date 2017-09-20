package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Gema on 26/01/2017.
 */

@DatabaseTable(tableName = "RMedicinesTable")
public class IRegisteredMedicines implements Comparable<IRegisteredMedicines> , Serializable {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int id_medicacion;
    @DatabaseField
    private int dosis ;
    @DatabaseField
    private int veces_dia ; // por ahora no se usa, se guarda valor en reminderTimeCode
    @DatabaseField
    private String fecha_consumo;
    @DatabaseField
    private String consumo_medicina ;

    @DatabaseField
    private String reminderTypeCode; // O - Frecuencia ; 1 - Intervalos
    @DatabaseField
    private String reminderTypeDescription; // O - Frecuencia ; 1 - Intervalos

    @DatabaseField
    private int reminderTimeCode; // valor del Spinner
    @DatabaseField
    private String reminderTimeDescription;

    @DatabaseField
    private String fechaInicio;
    @DatabaseField
    private String fechaFin; // NULL = Contunuo , Si contiene valor se obtienen los números de días.
    // String concatenado de los dias de la semana.
    // 0 = Todos
    // 1345 = Lunes, Miércoles, Jueves, Viernes
    @DatabaseField
    private String diasMedicacion;
    @DatabaseField
    private int medicineTypeCode;
    @DatabaseField
    private String medicineTypeDescription;
    @DatabaseField
    private String registeredMedicinesStatus;

    @DatabaseField
    private String email;
    @DatabaseField
    private String idUsuario;
    @DatabaseField
    private String sentWs;
    @DatabaseField
    private String operationDb;
    @DatabaseField
    private int idServerDb;

    public int getId_medicacion() {
        return id_medicacion;
    }

    public void setId_medicacion(int id_medicacion) {
        this.id_medicacion = id_medicacion;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public int getVeces_dia() {
        return veces_dia;
    }

    public void setVeces_dia(int veces_dia) {
        this.veces_dia = veces_dia;
    }

    public String getConsumo_medicina() {
        return consumo_medicina;
    }

    public void setConsumo_medicina(String consumo_medicina) {
        this.consumo_medicina = consumo_medicina;
    }

    public String getFecha_consumo() {
        return fecha_consumo;
    }

    public void setFecha_consumo(String fecha_consumo) {
        this.fecha_consumo = fecha_consumo;
    }

    public String getReminderTypeCode() {
        return reminderTypeCode;
    }

    public void setReminderTypeCode(String reminderTypeCode) {
        this.reminderTypeCode = reminderTypeCode;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDiasMedicacion() {
        return diasMedicacion;
    }

    public void setDiasMedicacion(String diasMedicacion) {
        this.diasMedicacion = diasMedicacion;
    }

    public int getMedicineTypeCode() {
        return medicineTypeCode;
    }

    public void setMedicineTypeCode(int medicineTypeCode) {
        this.medicineTypeCode = medicineTypeCode;
    }

    public String getMedicineTypeDescription() {
        return medicineTypeDescription;
    }

    public void setMedicineTypeDescription(String medicineTypeDescription) {
        this.medicineTypeDescription = medicineTypeDescription;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSentWs() {
        return this.sentWs;
    }

    public void setSentWs(String sentWs) {
        this.sentWs = sentWs;
    }

    public String getOperationDb () {
        return  this.operationDb;
    }

    public void setOperationDb ( String operationDb ) {
        this.operationDb = operationDb;
    }

    public int getIdServerDb() {
        return  this.idServerDb;
    }

    public void setIdServerDb(int idServerDb){
        this.idServerDb = idServerDb;
    }



    @Override
    public String toString(){
        return  "[" +
                "id=" + id +
                ",id_medicacion=" + id_medicacion +
                ",dosis=" + dosis  +
                ",veces_dia=" + veces_dia  +
                ",fecha_consumo=" + fecha_consumo +
                ",consumo_medicina=" + consumo_medicina  +
                ",reminderTypeCode=" + reminderTypeCode +
                ",reminderTypeDescription=" + reminderTypeDescription +
                ",reminderTimeCode=" + reminderTimeCode +
                ",reminderTimeDescription=" + reminderTimeDescription +
                ",fechaInicio=" + fechaInicio +
                ",fechaFin=" + fechaFin +
                ",diasMedicacion"+ diasMedicacion +
                ",medicineTypeCode"+ medicineTypeCode +
                ",medicineTypeDescription"+ medicineTypeDescription +
                ",email=" + email +
                ",idUsuario=" + idUsuario +
                ",sentWs=" + sentWs +
                ",operationDb=" + operationDb +
                ",idServerDb=" + idServerDb +
                "]" ;
    }


    //INI GRUPO ANTERIOR
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

    @Override
    public int compareTo(@NonNull IRegisteredMedicines o) {
        String a  = this.getNombre();
        String b  = o.getNombre();
        return a.compareTo(b);
    }

    public int getReminderTimeCode() {
        return reminderTimeCode;
    }

    public void setReminderTimeCode(int reminderTimeCode) {
        this.reminderTimeCode = reminderTimeCode;
    }

    public String getReminderTimeDescription() {
        return reminderTimeDescription;
    }

    public void setReminderTimeDescription(String reminderTimeDescription) {
        this.reminderTimeDescription = reminderTimeDescription;
    }

    public String getReminderTypeDescription() {
        return reminderTypeDescription;
    }

    public void setReminderTypeDescription(String reminderTypeDescription) {
        this.reminderTypeDescription = reminderTypeDescription;
    }

    public String getRegisteredMedicinesStatus() {
        return registeredMedicinesStatus;
    }

    public void setRegisteredMedicinesStatus(String registeredMedicinesStatus) {
        this.registeredMedicinesStatus = registeredMedicinesStatus;
    }

    //FIN GRUPO ANTERIOR

}
