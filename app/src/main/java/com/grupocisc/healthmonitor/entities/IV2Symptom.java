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
 * Created by Andres Onate GrupoLink on 09/04/2015.
 */

public interface IV2Symptom {

    @POST("diabetes/patientUsers/queryAsthmaSymptoms")
    Call<Obj> getSymptomFrom(@Body ObjPickFlow objPickFlow) ;

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
        String descripcion;
    }
}
