package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jesenia on 11/08/2017.
 */

public interface IRegWeight {

    @POST("diabetes/patientUsers/registerWeight")
    Call<RegWeight> setSendregisterWeightFrom(@Body rowPeso rPeso);

    @POST("diabetes/patientUsers/updateWeight")
    Call<RegWeight> setSendWeightUpdateFrom(@Body rowPesoUpdate rPeso);

    public class RegWeight{
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


