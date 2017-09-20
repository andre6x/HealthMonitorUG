package com.grupocisc.healthmonitor.entities;



import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Andres Onate GrupoLink on 09/04/2015.
 */

public interface IV2ConsultWeight {

    @POST("diabetes/patientUsers/queryWeightLog")
    Call<Obj> getConsultWeight(@Body ObjUserdate objUser);

    public class Obj {
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        List<rows> rows ;
    }


    public class rows {
        @Getter
        @Setter
        int idRegisterDB;
        @Getter
        @Setter
        float weight;
        @Getter
        @Setter
        float imc;
        @Getter
        @Setter
        float tmb;
        @Getter
        @Setter
        float waterPercentage;
        @Getter
        @Setter
        float greasePercentage;
        @Getter
        @Setter
        float dmo;
        @Getter
        @Setter
        float muscleMass;
        @Getter
        @Setter
        String date;
        @Getter
        @Setter
        String observations;

    }
}

