package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ISendBackAccount {
    @GET("control/diabetes/personas/envio_email_act_credencial")
    Call<SendBackAccount> EnvioEmail(@Query("email") String email);
    public class SendBackAccount{
        int codigo;
        String respuesta;
        String password;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }



    }
}
