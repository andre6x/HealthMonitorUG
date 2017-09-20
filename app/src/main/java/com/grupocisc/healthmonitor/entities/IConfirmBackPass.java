package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface IConfirmBackPass {

    @POST("diabetes/patientUsers/updateCredential")
    Call<RegistroNuevaPass> RegPass(@Body ObjNewPass objNewPass);

    public class RegistroNuevaPass {
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;


    }
}
