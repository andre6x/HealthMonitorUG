package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IConsulDoctorEspeciality {

    @POST("diabetes/patientUsers/querySpecialties")
    Call<Obj> CunsulParametEspeciality();


    public class Obj{
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        List<Especiality> rows;
    }

    public class Especiality{
        @Getter
        @Setter
        int specialtyId;
        @Getter
        @Setter
        String name;

    }
}
