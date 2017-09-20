package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface IConsulHba1c {


    @POST("diabetes/patientUsers/registerComplementaryExams")
    Call<Hba> setSendregisterHba1cFrom(@Body rowV2Hba1 rowV2Hba1);


    @POST("diabetes/patientUsers/updateComplementaryExams")
    Call<Hba> setSendregisterHba1cUpdateFrom(@Body rowV2Hba1Update rowV2Hba1Update);

    public class Hba{

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

