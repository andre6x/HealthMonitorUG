package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IV2RegistreState {

    @POST("diabetes/patientUsers/registerMood")
    Call<RegistroState> setSendStateFrom(@Body rowV2State rState);

    @POST("diabetes/patientUsers/updateMood")
    Call<RegistroState> setSendStateUpdateFrom(@Body rowV2StateUpdate rState);


    public class RegistroState {
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
