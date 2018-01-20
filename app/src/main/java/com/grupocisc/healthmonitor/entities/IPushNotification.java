package com.grupocisc.healthmonitor.entities;



import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by GrupoLink on 05/10/2015.
 */
public interface IPushNotification {


    @PUT("control/diabetes/notificaciones/inserta_actualiza_token")
    Call<InsertNotification> INSERT_NOTIFICATION_CALL( @Query("email") String email,
                                                       @Query("push") String push);

    class InsertNotification{
        @Getter
        @Setter
        private int codigo ;
        @Getter
        @Setter
        private String respuesta;

    }

    @GET("control/diabetes/mensajes_notificaciones/obtiene_mensaje")
    Call<List<NotifiMensajes>> getPushNotification_3(@Query("token") String token,
                                                     @Query("email") String email);

    class NotifiMensajes {
        @Getter
        @Setter
        String nombreDoctor;
        @Getter
        @Setter
        String mensajes;

    }



    @GET("controlProcesos/diabetes/procesos_oap/ejecuta_slc_tips")
    Call<List<TipsMensajes>> getTips (@Query("id_enfermedad") int id);

    class TipsMensajes {
        @Getter
        @Setter
        String url;
        @Getter
        @Setter
        String mensaje;

    }



    @GET("")
    Call<List<Recommendation>> getRecommendations(@Query("id_patient") String id_patient, @Query("id_section")int id_section);
    class Recommendation implements Serializable{
        @Getter
        @Setter
        String content;

        @Getter
        @Setter
        int id;
    }
}
