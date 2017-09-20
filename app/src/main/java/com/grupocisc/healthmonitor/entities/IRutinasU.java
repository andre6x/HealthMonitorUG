package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 06/03/2017.
 */

public interface IRutinasU {

    @GET("rutina/obtiene_rutina")
    Call<ArrayList<Integer>> getIdRutinas(@Query("id_enfermedad") int id);


    public class Rutinas{
        private ArrayList<Integer> idRutina;

        public ArrayList<Integer> getIdRutina() {
            return idRutina;
        }

        public void setIdRutina(ArrayList<Integer> idRutina) {
            this.idRutina = idRutina;
        }
    }
}
