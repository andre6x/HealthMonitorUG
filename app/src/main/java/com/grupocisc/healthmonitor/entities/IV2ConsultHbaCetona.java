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

public interface IV2ConsultHbaCetona {

    @POST("controlServices/diabetes/patientUsers/queryComplementaryExamsLog")
    Call<Obj> getConsultGlucosa(@Body ObjUserdate objUser);


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
        float hba1c;
        @Getter
        @Setter
        float cetonas;
        @Getter
        @Setter
        String date;
        @Getter
        @Setter
        String observations;

    }
}

