package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/****************************************************************************/
/*     Archivo:             EInsulin.java                                   */
/*     Módulo:              Medicina                                        */
/*     Disenado por:        Gema                                            */
/*     Fecha de escritura:  01 de Enero de 2017                             */
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
/*     06/Ene/2017     Gema                 Emision Inicial                 */
/*     06/Jun/2017     Marco Polo           Agregar Campos de de la base    */
/*                                          IdDB, ProcesoBD, EnviadoWs      */
/*                                                                          */
/*                                                                          */
/****************************************************************************/


@DatabaseTable (tableName = "InsulinTable")
public class EInsulin implements Serializable {

    @DatabaseField(generatedId=true)
    @Getter
    @Setter
    private int id;
    @DatabaseField
    @Getter
    @Setter
    private int idBdServer;
    @DatabaseField
    @Getter
    @Setter
    private int insulina;
    @DatabaseField
    @Getter
    @Setter
    private String observacion;
    @DatabaseField
    @Getter
    @Setter
    private String fecha;
    @DatabaseField
    @Getter
    @Setter
    private String hora;
    @DatabaseField
    @Getter
    @Setter
    private String enviadoServer;
    @DatabaseField
    @Getter
    @Setter
    private String operationDb;

    @Override
    public String toString(){
        return "[" +
                "id=" + this.id +
                ", insulina=" + this.insulina +
                ", observacion=" + this.observacion +
                ", fecha=" + this.fecha +
                ", hora=" + this.hora +
                ", enviadoServer=" + this.enviadoServer +
                ", operationDb=" + this.operationDb +
                ", idBdServer=" + this.idBdServer +
                "]";
    }


}
