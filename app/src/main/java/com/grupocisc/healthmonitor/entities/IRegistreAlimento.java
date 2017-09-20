package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Walter on 08/03/2017.
 */

public interface IRegistreAlimento {

    @PUT("alimento_paciente/vincula")
    Call<RegistroAlimento> putAlimento(@Query("email") String email,
                                       @Query("descripcion") String descripcion,
                                       @Query("porcion") float porcion,
                                       @Query("calorias") float calorias,
                                       @Query("proteinas") float proteinas,
                                       @Query("grasas") float grasas,
                                       @Query("carbohidratos") float carbohidratos,
                                       @Query("fecha") String fecha);

    public class RegistroAlimento{

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
