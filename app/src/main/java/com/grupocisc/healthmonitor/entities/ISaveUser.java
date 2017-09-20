package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by User on 10/04/2015.
 */
public interface ISaveUser {


    @FormUrlEncoded
    @POST("saveInfoSubscriber")
    Call<SaveUser> saveDataUser(@Query("msisdn") String user_email,
                                 @Query("cod_servicio") String user_nombre,
                                 @Field("name") String user_apellido,
                                 @Field("lastname") String user_anio,
                                 @Field("peso") String user_peso,
                                 @Field("altura") String user_altura,
                                 @Field("sexo") String user_sexo);

    @GET("verifyPIN")
    Call<SaveUser> saveDataUser_2(@Query("msisdn") String msisdn,
                                 @Query("pin")    String pin,
                                 @Query("cod_servicio") String CodigoServicio,
                                 @Query("channel") String channel);

    public class SaveUser{
        int code;
        String message_alert;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage_alert() {
            return message_alert;
        }

        public void setMessage_alert(String message_alert) {
            this.message_alert = message_alert;
        }
    }


}
