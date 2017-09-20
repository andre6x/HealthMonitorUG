package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 13/03/2017.
 */

public interface IDietR {

    @GET("procesos_oap/ejecuta_RecAlimentacion")
    Call<ArrayList<IDietR.DietR>> getDietR(@Query("email") String email);

    public class DietR{
        private String idDieta;
        private String nombre;
        private String descripcion;
        private String calorias;
        private String grasas;
        private String carbohidratos;
        private String proteinas;
        private String observaciones;
        private String url;

        public String getIdDieta() {
            return idDieta;
        }

        public void setIdDieta(String idDieta) {
            this.idDieta = idDieta;
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

        public String getCalorias() {
            return calorias;
        }

        public void setCalorias(String calorias) {
            this.calorias = calorias;
        }

        public String getGrasas() {
            return grasas;
        }

        public void setGrasas(String grasas) {
            this.grasas = grasas;
        }

        public String getCarbohidratos() {
            return carbohidratos;
        }

        public void setCarbohidratos(String carbohidratos) {
            this.carbohidratos = carbohidratos;
        }

        public String getProteinas() {
            return proteinas;
        }

        public void setProteinas(String proteinas) {
            this.proteinas = proteinas;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
