package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */

public interface IV2ConsulControlMedication {
    
    @POST("diabetes/patientUsers/queryControlMedication")
    Call<Obj> queryControlMedication(@Body ObjQueryControlMedication obj);
    @ToString
    class Obj {
        @Getter
        @Setter
        int idCodResult ;           // 0
        @Getter
        @Setter
        String resultDescription;   // "Sus datos se registraron con exito"
        @Getter
        @Setter
        List<Rows> rows;          // 154
    }
    @ToString
    class Rows{
        @Getter
        @Setter
        int         idRegisterDB; //": 154,
        @Getter
        @Setter
        int         idAlarmMedication; //10,
        @Getter
        @Setter
        int         medicineID; //6,
        @Getter
        @Setter
        String      consumedMedicine; //"S",
        @Getter
        @Setter
        String      date; //"2017-11-26 10:57:03.0",
        @Getter
        @Setter
        String      observations; //": "PRUEBA REGISTRO MEDICACION",

        public Rows(){
        }

        public Rows(int idRegisterDB,int idAlarmMedication,int medicineID,String consumedMedicine,String date,String observations){
            this.idRegisterDB = idRegisterDB;
            this.idAlarmMedication = idAlarmMedication;
            this.medicineID = medicineID;
            this.consumedMedicine = consumedMedicine;
            this.date = date;
            this.observations = observations;

        }


    }
    @ToString
    class ObjQueryControlMedication {
        @Getter
        @Setter
        String      identifier ;            // 109jordy@gmail.com ,

        public ObjQueryControlMedication(String identifier) {
            this.identifier = identifier;
        }
    }

}
