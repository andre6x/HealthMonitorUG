package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IDesvinculaDr {

    @POST("controlServices/diabetes/patientUsers/disassociateDoctorPatient")
    Call<DesvinculaDoctor> DesvDoctor(@Body ObjDoctorSelect objDoctorSelect);

    public class DesvinculaDoctor {
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;

    }
}
