package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 09/03/2017.
 */

public interface IGetFeeding {

    @GET("alimento_paciente/lista_alimento_paciente")
    Call<ArrayList<RegistreFeeding>> getFeeding(@Query("email") String email);

    public class RegistreFeeding{
        private String descripcion;
        private float porcion;
        private float calorias;
        private float grasas;

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public float getPorcion() {
            return porcion;
        }

        public void setPorcion(float porcion) {
            this.porcion = porcion;
        }

        public float getCalorias() {
            return calorias;
        }

        public void setCalorias(float calorias) {
            this.calorias = calorias;
        }

        public float getGrasas() {
            return grasas;
        }

        public void setGrasas(float grasas) {
            this.grasas = grasas;
        }
    }
}
