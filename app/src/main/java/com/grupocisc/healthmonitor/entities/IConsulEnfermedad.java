package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IConsulEnfermedad {

    @GET("control/diabetes/enfermedad/")
    Call<List<Enfermedad>> CunsulParamet();

    public class Enfermedad {
        int idEnfermedad;
        String nombre;
        String descripcion;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getIdEnfermedad() {
            return idEnfermedad;
        }

        public void setIdEnfermedad(int idEnfermedad) {
            this.idEnfermedad = idEnfermedad;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }




    }
}
