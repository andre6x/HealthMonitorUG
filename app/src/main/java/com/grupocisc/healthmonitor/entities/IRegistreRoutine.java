package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Walter on 08/03/2017.
 */

public interface IRegistreRoutine {

    @PUT("control/diabetes/rutina/vincula_rutina_ejercicio")
    Call<RegistroRoutine> putRoutine(@Query("email") String email,
                                     @Query("id_rutina") int id_rutina,
                                     @Query("calificacion") int calificacion);

    public class RegistroRoutine{

        private int codigo;
        private String respuesta;

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }
}
