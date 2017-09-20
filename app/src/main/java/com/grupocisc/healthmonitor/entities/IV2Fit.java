package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jesenia on 23/08/2017.
 */

public interface IV2Fit {

    @POST("diabetes/patientUsers/registerGoogleFit")
    Call<Fit> setSendFitFrom(@Body rowV2Fit rFit);


    public class Fit{
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
