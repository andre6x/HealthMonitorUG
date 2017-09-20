package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mariuxi on 23/02/2017.
 */

public interface IPesoIdeal {
    @GET("pIdeal")
    Call<GetPesoIdeal> ConPesoIdeal(@Query("email") String email);

    public class GetPesoIdeal{
        int codigo;
        String respuesta;
        String peIdeal;
        String diferencia;

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

        public String getPesoIdeal() {
            return peIdeal;
        }

        public void setPesoIdeal(String pesoIdeal) {
            this.peIdeal = pesoIdeal;
        }

        public String getDiferencia() {
            return diferencia;
        }

        public void setDiferencia(String diferencia) {
            this.diferencia = diferencia;
        }
    }

}
