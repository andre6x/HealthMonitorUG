package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Walter on 01/03/2017.
 */

public interface IDietU {

    @GET("dieta")
    Call<ArrayList<IDietU.Diet>> getListDiet();

    //@DatabaseTable(tableName = "DietTable")
    public class Diet{
        private int idDieta;
        private String nombre;
        private String descripcion;
        private float calorias;
        private float grasas;
        private float carbohidratos;
        private float proteinas;
        private String observaciones;
        private String url;
        private String key;

        public int getIdDieta() {
            return idDieta;
        }

        public void setIdDieta(int idDieta) {
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

        public float getCarbohidratos() {
            return carbohidratos;
        }

        public void setCarbohidratos(float carbohidratos) {
            this.carbohidratos = carbohidratos;
        }

        public float getProteinas() {
            return proteinas;
        }

        public void setProteinas(float proteinas) {
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

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
