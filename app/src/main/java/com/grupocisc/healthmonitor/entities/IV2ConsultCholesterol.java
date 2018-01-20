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

public interface IV2ConsultCholesterol {

    @POST("controlServices/diabetes/patientUsers/queryCholesterolLog")
    Call<Obj> getConsultCholesterol(@Body ObjUserdate objUser);


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
        int cholesterol;
        @Getter
        @Setter
        int triglycerides;
        @Getter
        @Setter
        int hdl;
        @Getter
        @Setter
        int ldl;
        @Getter
        @Setter
        String date;
        @Getter
        @Setter
        String observations;


    }
}

