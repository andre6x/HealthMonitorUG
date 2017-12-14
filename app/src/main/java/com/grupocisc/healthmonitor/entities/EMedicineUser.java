package com.grupocisc.healthmonitor.entities;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/****************************************************************************/
/*     Archivo:             EMedicineUser.java                              */
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

//@DatabaseTable(tableName = "MedicineUserTable")
@DatabaseTable(tableName = Constantes.TABLA_MEDICINE_USER)

public class EMedicineUser {
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int idMedicacion;
    @DatabaseField
    private String fechaRegistro;
    @DatabaseField
    private String medicineUserStatus;
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMedicacion() {
        return this.idMedicacion;
    }

    public void setIdMedicacion(int idMedicacion) {
        this.idMedicacion = idMedicacion;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return this.idUsuario;
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
        return "[" +
                " id = " + id +
                " idMedicacion = " + idMedicacion +
                " fechaRegistro = " +  fechaRegistro +
                " email = " + email +
                " idUsuario = " +  idUsuario +
                " sentWs = " + sentWs +
                " operationDb = " + operationDb +
                " idServerDb = " + idServerDb +
                "]" ;
    }

    public String getMedicineUserStatus() {
        return medicineUserStatus;
    }

    public void setMedicineUserStatus(String medicineUserStatus) {
        this.medicineUserStatus = medicineUserStatus;
    }
}
