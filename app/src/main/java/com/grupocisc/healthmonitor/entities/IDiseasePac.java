package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IDiseasePac {

    @GET("control/diabetes/enfermedad_pacientes/datos_enfermedad_paciente/")
    Call<List<Disease>> DiseasePac(@Query("email") String email);


    public class Disease implements Comparable<Disease>{
        String nombre;
        String descripcion;
        String observacion;
        String fechaAparicion;
        String fechaCura;


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

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

        public String getFechaCura() {
            return fechaCura;
        }

        public void setFechaCura(String fechaCura) {
            this.fechaCura = fechaCura;
        }

        public String getFechaAparicion() {
            return fechaAparicion;
        }

        public void setFechaAparicion(String fechaAparicion) {
            this.fechaAparicion = fechaAparicion;
        }

        @Override
        public int compareTo(@NonNull Disease o) {
            String a  = this.getFechaAparicion();
            String b  = o.getFechaAparicion();
            return b.compareTo(a);
        }
    }
}
