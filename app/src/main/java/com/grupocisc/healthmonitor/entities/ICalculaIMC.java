package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mariuxi on 24/02/2017.
 */

public interface ICalculaIMC {

    @GET("RecomPesoImc")
    Call <List<GetImc>> ConsulImc(@Query("email") String email);

    public class GetImc{
        int idPaciente;
        float imc;
        float rangoMin;
        float rangoMax;
        String mensaje;
        String tipoPeso;

        public int getIdPaciente() {
            return idPaciente;
        }

        public void setIdPaciente(int idPaciente) {
            this.idPaciente = idPaciente;
        }

        public float getImc() {
            return imc;
        }

        public void setImc(float imc) {
            this.imc = imc;
        }

        public float getRangoMin() {
            return rangoMin;
        }

        public void setRangoMin(float rangoMin) {
            this.rangoMin = rangoMin;
        }

        public float getRangoMax() {
            return rangoMax;
        }

        public void setRangoMax(float rangoMax) {
            this.rangoMax = rangoMax;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getTipoPeso() {
            return tipoPeso;
        }

        public void setTipoPeso(String tipoPeso) {
            this.tipoPeso = tipoPeso;
        }
    }

}

