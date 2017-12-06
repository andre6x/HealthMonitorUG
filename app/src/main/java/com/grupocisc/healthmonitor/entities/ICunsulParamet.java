package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 10/04/2015.
 */
public interface ICunsulParamet {

    @GET("control/diabetes/personas/obtiene_estadistica")
    Call<List<Objeto>> CunsulParamet(@Query("email") String email,
                                     @Query("parametro") String parametro,
                                     @Query("fecha_inicio") String fecha_inicio,
                                     @Query("fecha_fin") String fecha_fin
    ) ;

    //http://181.39.136.237:5678/control/diabetes/personas/obtiene_estadistica_total?email=laura@hotmail.com&parametro=glucosa&fecha_inicio=2017/01/01%2012:30:00&fecha_fin=2017/03/09%2012:30:00
    @GET("control/diabetes/personas/obtiene_estadistica_total")
    Call<List<Objeto>> HistorialDatos(@Query("email") String email,
                                     @Query("parametro") String parametro,
                                     @Query("fecha_inicio") String fecha_inicio,
                                     @Query("fecha_fin") String fecha_fin
    ) ;

    class Objeto implements Comparable<Objeto>{

        float valor;
        String fecha;
        String medio;
        String observacion;


        public float getValor() {
            return valor;
        }

        public void setValor(float valor) {
            this.valor = valor;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getMedio() {
            return medio;
        }

        public void setMedio(String medio) {
            this.medio = medio;
        }

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

        @Override
        public int compareTo(@NonNull Objeto o) {
            String a  = this.getFecha();
            String b  = o.getFecha();
            return b.compareTo(a);
        }
    }


}
