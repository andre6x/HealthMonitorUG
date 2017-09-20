package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mariuxi on 04/03/2017.
 */

public interface IRecomenPressure {


    @GET("RePresion")
    Call<Objeto> RecomenPressure(@Query("email") String email);

    public class Objeto {

        int codigo;
        String respuesta;
        String mensaje;

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

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
