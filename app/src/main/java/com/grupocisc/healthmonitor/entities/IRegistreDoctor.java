package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IRegistreDoctor {

    @POST("controlServices/diabetes/patientUsers/associateDoctorPatient")
    Call<RegistroDoctor> RegDoctor(@Body ObjDoctorSelect objDoctorSelect);

    public class RegistroDoctor{

        @Getter
        @Setter
        int idCodResult ;
        @Getter
        @Setter
        String resultDescription ;


    }

}
