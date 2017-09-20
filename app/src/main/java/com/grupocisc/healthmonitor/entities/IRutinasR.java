package com.grupocisc.healthmonitor.entities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 14/03/2017.
 */

public interface IRutinasR {

    @GET("procesos_oap/ejecuta_RecRutina")
    Call<ArrayList<String>> getIdRutinas(@Query("email") String email);

    public class RutinasR{
        private String key;
        private int value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        /*
        private ArrayList<String> idRutina;

        public ArrayList<String> getIdRutina() {
            return idRutina;
        }

        public void setIdRutina(ArrayList<String> idRutina) {
            this.idRutina = idRutina;
        }
        */

    }
}
