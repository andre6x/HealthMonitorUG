package com.grupocisc.healthmonitor.entities;

/**
 * Created by Jesenia on 29/07/2017.
 */

 import com.grupocisc.healthmonitor.Utils.Constantes;
 import com.j256.ormlite.field.DatabaseField;

 import com.j256.ormlite.stmt.query.In;
 import com.j256.ormlite.table.DatabaseTable;


 import java.io.Serializable;
 import java.sql.Date;

 //@DatabaseTable(tableName = "AsthmaTable")
 @DatabaseTable(tableName = Constantes.TABLA_ASTHMA)

 public class IAsthma implements Serializable {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int  idBdServer;
    @DatabaseField
    private float flujoMaximo;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String hora;
    @DatabaseField
    private String observacion;
    @DatabaseField
    private String enviadoServer ;
    @DatabaseField
    private String operationDb;

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

     public float getFlujoMaximo() {
         return flujoMaximo;
     }

     public void setFlujoMaximo(float flujoMaximo) {
         this.flujoMaximo = flujoMaximo;
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

     public String getOperationDb() {
         return operationDb;
     }

     public void setOperationDb(String operationDb) {
         this.operationDb = operationDb;
     }
 }
