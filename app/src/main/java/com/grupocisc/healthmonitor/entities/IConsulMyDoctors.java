package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IConsulMyDoctors {

    @POST("diabetes/patientUsers/queryAssociatedDoctor")
    Call<Obj> getConsultMyDoctors(@Body ObjUser objUser);


    public class Obj{
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        List<rows> rows;
    }

    public class rows{
        @Getter
        @Setter
        int doctorId;
        @Getter
        @Setter
        String name;
        @Getter
        @Setter
        String lastName;
        @Getter
        @Setter
        String specialty;
        @Getter
        @Setter
        String email;
        @Getter
        @Setter
        String cellPhone;



    }
}
