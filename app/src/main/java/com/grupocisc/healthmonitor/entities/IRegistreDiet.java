package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Walter on 08/03/2017.
 */

public interface IRegistreDiet {

    @PUT("control/diabetes/dieta_paciente/inserta_dieta_paciente")
    Call<RegistreDiet> putDiet(@Query("email") String email,
                               @Query("id_dieta") int id_dieta,
                               @Query("calificacion") int calificacion);

    public class RegistreDiet{
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
