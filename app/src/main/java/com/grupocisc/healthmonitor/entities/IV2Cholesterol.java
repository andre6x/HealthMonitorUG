package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jesenia on 13/08/2017.
 */

public interface IV2Cholesterol {
    @POST("controlServices/diabetes/patientUsers/registerCholesterol")
    Call<Cholesterol> setSendCholesterolFrom(@Body rowV2Cholesterol rCholesterol);

    @POST("controlServices/diabetes/patientUsers/updateCholesterol")
    Call<Cholesterol> setSendCholesterolupdateFrom(@Body rowV2CholesterolUpdate rCholesterolupdate);


    public class Cholesterol{
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
