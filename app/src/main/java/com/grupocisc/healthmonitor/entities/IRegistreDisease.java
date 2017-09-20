package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface IRegistreDisease {

    @PUT("control/diabetes/enfermedad_pacientes/inserta_enfermedad_paciente")
    Call<RegistreDisease> RegDisease(@Query("email") String email,
                                   @Query("fecha_aparicion") String fecha_aparicion,
                                   @Query("observacion") String observacion,
                                   @Query("fecha_cura") String fecha_cura,
                                   @Query("id_enfermedad") int id_enfermedad);

    public class RegistreDisease{
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
