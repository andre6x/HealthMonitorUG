package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HP on 04/03/2017.
 */

public interface IConsulState {
    @GET("procesos_oap/animo_paciente_andr")
    Call<List<IConsulState.Objeto>> ConsulState(@Query("email") String email,
                                                @Query("fechaDesde") String fechaDesde,
                                                @Query("fechaHasta") String fechaHasta
    ) ;

    public class Objeto{

        int idEstadoAnimo;
        String fecha;

        public int getidEstadoAnimo() {
            return idEstadoAnimo;
        }

        public void setidEstadoAnimo(int idEstadoAnimo) {
            this.idEstadoAnimo = idEstadoAnimo;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }


    }
}
