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
public interface IUserLogin {


    @POST("controlServices/diabetes/patientUsers/validateAccount")
    Call<UserLogin> LoginUser(@Body ObjLogin rPeso);
    /*
    @GET("control/diabetes/personas/verifica_paciente")
    Call<UserLogin> LoginUser(@Query("email") String email,
                              @Query("credencial") String credencial);
    */

    public class UserLogin{
        @Getter
        @Setter
        int idCodResult ;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        String  name;
        @Getter
        @Setter
        String lastName;
        @Getter
        @Setter
        String email;
        @Getter
        @Setter
        String gender;
        @Getter
        @Setter
        String birthDate;
        @Getter
        @Setter
        float height;
        @Getter
        @Setter
        float weight;
        @Getter
        @Setter
        String relationshipStatus;
        @Getter
        @Setter
        String cellPhone;
        @Getter
        @Setter
        String country;
        @Getter
        @Setter
        int diabetesType;
        @Getter
        @Setter
        int asma;
        @Getter
        @Setter
        String url;
        @Getter
        @Setter
        int idPersonDB;
        @Getter
        @Setter
        int idPatientDB;
        @Getter
        @Setter
        String identifier;

    }


}
