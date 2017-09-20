package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IRegistrePerson {
//http://157.55.171.107:8080/controlServices/diabetes/patientUsers/registerPatientUser

    @POST("diabetes/patientUsers/registerPatientUser")
    Call<RegistroPersona> RegistroPersona1(@Body ObjDataUserLogin objetoDataUserLogin);

    class RegistroPersona{
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        int idPersonDB;
        @Getter
        @Setter
        int  idPatientDB;

    }

    class ObjDataUserLogin {
        @Getter
        @Setter
        String name                 ; // JESENIA ,
        @Getter
        @Setter
        String lastName             ; // QUITO ,
        @Getter
        @Setter
        String email                ; // jesequito2@hotmail.com ,
        @Getter
        @Setter
        String password             ; // 123456 ,
        @Getter
        @Setter
        String birthDate            ; // 1992-09-05 ,
        @Getter
        @Setter
        String identifier           ; // 0912345678 ,
        @Getter
        @Setter
        float weight                ; //70,
        @Getter
        @Setter
        float height                ; //1.65,
        @Getter
        @Setter
        String gender               ; // F ,
        @Getter
        @Setter
        String relationshipStatus   ; // SOLTERO ,
        @Getter
        @Setter
        String cellPhone            ; // 0900000000 ,
        @Getter
        @Setter
        int countryId            ; //1,
        @Getter
        @Setter
        int diabetesType         ; //11,
        @Getter
        @Setter
        int asma                 ; // 2,
        @Getter
        @Setter
        String url                  ; //  www.test.com
        public ObjDataUserLogin(String name, String lastName, String email, String password, String birthDate, String identifier, float weight, float height, String gender, String relationshipStatus, String cellPhone, int countryId, int diabetesType, int asma, String url) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.birthDate = birthDate;
            this.identifier = identifier;
            this.weight = weight;
            this.height = height;
            this.gender = gender;
            this.relationshipStatus = relationshipStatus;
            this.cellPhone = cellPhone;
            this.countryId = countryId;
            this.diabetesType = diabetesType;
            this.asma = asma;
            this.url = url;
        }


    }




}
