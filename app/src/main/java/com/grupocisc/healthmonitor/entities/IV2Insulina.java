package com.grupocisc.healthmonitor.entities;

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
public interface IV2Insulina {

    @POST("controlServices/diabetes/patientUsers/registerInsulin")
    Call<Insulina> setSendInsulinFrom(@Body rowInsulin rInsulin);

    @POST("controlServices/diabetes/patientUsers/updateInsulin")
    Call<Insulina> setSendInsulinUpdateFrom(@Body rowInsulinUpdate rInsulin);

    public class  Insulina{

       @Getter
       @Setter
       int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        int idRegisterDB ;

    }


}
