package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/****************************************************************************/
/*     Archivo:             EMedicine.java                                  */
/*     Módulo:              Medicina                                        */
/*     Disenado por:        Marco Polo                                      */
/*     Fecha de escritura:  06 de Junio de 2017                             */
/****************************************************************************/
/*                            IMPORTANTE                                    */
/*     Este programa es parte de los paquetes educativos propiedad de       */
/*     "Universidad de Guayaquil" de la aplicación HealtMonitor.            */
/*                                                                          */
/*     Su uso no autorizado queda expresamente prohibido asi como           */
/*     cualquier alteracion o agregado hecho por alguno de sus usuarios     */
/*     sin el debido consentimiento por escrito de la Presidente Ejecutiva  */
/*     de la Universidad de Guayaquil o su represente.                      */
/****************************************************************************/
/*                           PROPOSITO                                      */
/*    Clase para el mapeo de datos de las medicinas                         */
/*                                                                          */
/****************************************************************************/
/*                           MODIFICACIONES                                 */
/*       FECHA                 AUTOR              RAZON                     */
/*     06/Jun/2017     Marco Polo           Emision Inicial                 */
/*     06/Jun/2017     Marco Polo           Agregar Campos de de la base    */
/*                                          IdDB, ProcesoBD, EnviadoWs      */
/*                                                                          */
/*                                                                          */
/****************************************************************************/

//@DatabaseTable(tableName = "MedicineTable")
@DatabaseTable(tableName = Constantes.TABLA_MEDICINE)

public class EMedicine implements Comparable<EMedicine>  {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int idMedicamento;
    @DatabaseField
    private String nombre;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String principioActivo ;
    @DatabaseField
    private String indicaciones ;
    @DatabaseField
    private String recomendaciones ;
    @DatabaseField
    private String via ;
    @DatabaseField
    private String presentacion ;
    @DatabaseField
    private String estado ;
    @DatabaseField
    private String laboratorio ;
    @DatabaseField
    private String email;
    @DatabaseField
    private String idUsuario;
    @DatabaseField
    private int medicineTypeCode;
    @DatabaseField
    private String medicineTypeDesc;
    @DatabaseField
    private String sentWs;
    @DatabaseField
    private String operationDb;
    @DatabaseField
    private int idServerDb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
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

    public int getMedicineTypeCode() {
        return medicineTypeCode;
    }

    public void setMedicineTypeCode(int medicineTypeCode) {
        this.medicineTypeCode = medicineTypeCode;
    }

    public String getMedicineTypeDesc() {
        return medicineTypeDesc;
    }

    public void setMedicineTypeDesc(String medicineTypeDesc) {
        this.medicineTypeDesc = medicineTypeDesc;
    }

    @Override
    public int compareTo(@NonNull EMedicine o) {
        String a  = this.getNombre();
        String b  = o.getNombre();
        return a.compareTo(b);
    }

    @Override
    public String toString(){
        return "[ " +
                " codigo = " + this.getId() +
                " idMedicina = " + this.getIdMedicamento() +
                " Nombre = " + this.getNombre() +
                " Presentación = " + this.getPresentacion() +
                " Via = " + this.getVia() +
                 " ]" ;
    }

}
