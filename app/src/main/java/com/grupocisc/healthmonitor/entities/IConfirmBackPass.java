package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface IConfirmBackPass {

    @POST("diabetes/patientUsers/updateCredential")
    Call<RegistroNuevaPass> RegPass(@Body ObjNewPass objNewPass);

    public class RegistroNuevaPass {
        int codigo;
        String respuesta;
        String email;
        String credencial;


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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCredencial() {
            return credencial;
        }

        public void setCredencial(String credencial) {
            this.credencial = credencial;
        }

    }
}
