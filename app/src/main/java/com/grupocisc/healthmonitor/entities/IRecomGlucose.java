package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 10/04/2015.
 */
public interface IRecomGlucose {

    @POST("diabetes/patientUsers/registerGlucose")
    Call<RecomGlucose> setSendregisterGlucosaFrom(@Body rowGlucosa rGlucosa);


    @POST("diabetes/patientUsers/updateGlucose")
    Call<RecomGlucose> setSendregisterGlucosaUpdateFrom(@Body rowGlucosaUpdate rGlucosa);

    public class RecomGlucose{

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
