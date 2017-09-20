package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 06/03/2017.
 */

public interface IRoutinesLessonU {

    @GET("rutina/obtiene_ejercicio_x_rutina")
    Call<ArrayList<RoutineExercice>> getRoutineExercice(@Query("id_rutina") int idRutina);

    public class RoutineExercice {
        private String nombre;
        private String url;
        private String repeticiones;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRepeticiones() {
            return repeticiones;
        }

        public void setRepeticiones(String repeticiones) {
            this.repeticiones = repeticiones;
        }
    }
}
