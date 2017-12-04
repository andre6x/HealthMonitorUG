package com.grupocisc.healthmonitor.entities;

import org.json.JSONArray;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Andres Onate GrupoLink on 09/04/2015.
 */

public interface IV2SavePickFlow {

    @POST("controlServices/diabetes/patientUsers/registerPeakFlow")
    Call<SavePickFlow> setSaveSavePickFlow(@Body ObjtSavePickFlow objtSavePickFlow );

    public class SavePickFlow {
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
