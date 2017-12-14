package com.grupocisc.healthmonitor.entities;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by aonate on 07/06/2017.
 */

    //@DatabaseTable(tableName = "ColesterolTable")
    @DatabaseTable(tableName = Constantes.TABLA_COLESTEROL)

    public class IColesterol implements Serializable {

        @DatabaseField(generatedId = true)
        private int id;
        @DatabaseField
        private int idBdServer;
        @DatabaseField
        private int colesterol;
        @DatabaseField
        private int hdl;
        @DatabaseField
        private int ldl;
        @DatabaseField
        private int triglycerides;
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

        public IColesterol() {
        }

        public int getId() {
                return id;
            }

        public void setId(int id) {
            this.id = id;
        }

        public int getColesterol() {
            return colesterol;
        }

        public void setColesterol(int colesterol) {
            this.colesterol = colesterol;
        }

        public int getHdl() {
            return hdl;
        }

        public void setHdl(int hdl) {
            this.hdl = hdl;
        }

        public int getLdl() {
            return ldl;
        }

        public void setLdl(int ldl) {
            this.ldl = ldl;
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

        public int getIdBdServer() {
            return idBdServer;
        }

        public void setIdBdServer(int idBdServer) {
            this.idBdServer = idBdServer;
        }

        public String getOperacion() {
            return operacion;
        }

        public void setOperacion(String operacion) {
            this.operacion = operacion;
        }

    public int getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(int triglycerides) {
        this.triglycerides = triglycerides;
    }
}

