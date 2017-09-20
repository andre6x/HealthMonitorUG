package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface IConsulNum {

    @GET("control/diabetes/personas/verifica_correo_telefono")
    Call<IConsulNum.ConsultaNum> ConsultaNumero(@Query("email") String email,
                                            @Query("telefono") String telefono);

    public class ConsultaNum {

        int codigo;
        String respuesta;


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
