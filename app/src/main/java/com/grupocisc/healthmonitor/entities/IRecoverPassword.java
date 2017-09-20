package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 10/04/2015.
 */
public interface IRecoverPassword {


    @GET("verifyPIN")
    Call<RecoverPassword> sendPassUser(@Query("msisdn") String msisdn,
                                  @Query("pin") String pin,
                                  @Query("cod_servicio") String CodigoServicio,
                                  @Query("channel") String channel);

    public class RecoverPassword{
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
