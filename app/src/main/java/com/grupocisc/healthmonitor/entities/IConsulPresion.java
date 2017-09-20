package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface IConsulPresion {
    @GET("procesos_oap/ejecuta_slcPresion")
    Call<List<Objeto>> CunsulPresion(@Query("email") String email,
                                  //@Query("parametro") String parametro,
                                  @Query("fechaDesde") String fechaDesde,
                                  @Query("fechaHasta") String fechaHasta
    ) ;

    public class Objeto{
        //  List<Usuario> LsiataUsuario;


        float sistolica;
        float diastolica;
        String fecha;
        //String medio;
        //String observacion;


        public float getSistolica() {
            return sistolica;
        }

        public void setSistolica(float sistolica) {
            this.sistolica = sistolica;
        }

        public float getDiastolica() {
            return diastolica;
        }

        public void setDiastolica(float diastolica) {
            this.diastolica = diastolica;
        }


        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }


       /* public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }
*/
    }



}

