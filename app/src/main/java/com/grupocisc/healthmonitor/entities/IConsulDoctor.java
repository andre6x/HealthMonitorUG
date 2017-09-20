package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IConsulDoctor {

    @POST("diabetes/patientUsers/queryDoctorsBySpecialty")
    Call<Obj> CunsulParamet(@Body ObjSpeciality objSpeciality);


    public class Obj{
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        List<doctor> rows;
    }

    public class doctor{
        @Getter
        @Setter
        int doctorId;
        @Getter
        @Setter
        String specialty;
        @Getter
        @Setter
        String name;
        @Getter
        @Setter
        String lastName;
        @Getter
        @Setter
        String email;
        @Getter
        @Setter
        String cellPhone;

    }
}
