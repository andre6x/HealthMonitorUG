package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by GrupoLink on 09/04/2015.
 */

public interface ISendPulsePresion {

    @POST("controlServices/diabetes/patientUsers/registerPressure")
    Call<SendPulsePresion> setSendPulsePresionFrom(@Body rowPulsePresion rPressure);

    @POST("controlServices/diabetes/patientUsers/updatePressure")
    Call<SendPulsePresion> setSendPulsePresionUpdateFrom(@Body rowPulsePresionUpdate rPeso);


    public class SendPulsePresion {

        @Getter
        @Setter
        private int idCodResult;
        @Getter
        @Setter
        private String resultDescription;
        @Getter
        @Setter
        private int idRegisterDB;


    }
}
