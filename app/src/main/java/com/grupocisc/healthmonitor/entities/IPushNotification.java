package com.grupocisc.healthmonitor.entities;



import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


    @POST("controlServices/diabetes/patientUsers/contentFiltering")
    Call<RecommendationRequest> getFiltering(@Body() ParamRequest parametros);


    @POST("controlServices/diabetes/patientUsers/recommendations")
    Call<RecommendationRequest> getRecommendations(@Body() ParamRequest parametros);
    class RecommendationRequest implements Serializable{
        @Getter
        @Setter
        String resultDescription;

        @Getter
        @Setter
        int idCodResult;

        @Getter
        @Setter
        public List<rows> rows;




    }

    class rows implements Serializable{
        @Getter
        @Setter
        public String recommendations;




    }


    class Recommendation implements Serializable{
        @Getter
        @Setter
        public String content;

        @Getter
        @Setter
        public int id;

//        @Getter
//        @Setter
//        public  String title;
    }

    class ParamRequest implements Serializable{

        public ParamRequest(String identifier, int idSection)
        {

            this.identifier = identifier;
            this.idSection = idSection;
        }
        @Getter
        @Setter
        public String identifier;

        @Getter
        @Setter
        public int idSection;
    }



}
