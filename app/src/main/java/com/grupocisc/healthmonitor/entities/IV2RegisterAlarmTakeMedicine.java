package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */
public interface IV2RegisterAlarmTakeMedicine {
    
    @POST("controlServices/diabetes/patientUsers/registerControlMedication")
    Call<registerAlarmTakeMedicine> registerAlarmTakeMedicine(@Body ObjRegisterAlarmTakeMedicine obj);
    class registerAlarmTakeMedicine{
        @Getter
        @Setter
        int idCodResult ;           // 0
        @Getter
        @Setter
        String resultDescription;   // "Sus datos se registraron con exito"
        @Getter
        @Setter
        int  idRegisterDB;          // 154
    }

    class ObjRegisterAlarmTakeMedicine {
        @Getter
        @Setter
        int      idAlarmMedication ;            // 109jordy@gmail.com ,
        @Getter
        @Setter
        int         medicationID   ;          //154,
        @Getter
        @Setter
        String      consumedMedicine ;          //08,
        @Getter
        @Setter
        String      date ;         // 0 ,
        @Getter
        @Setter
        String      observations ;          // prueba registro medicacion

        public ObjRegisterAlarmTakeMedicine(int idAlarmMedication, int medicationID, String consumedMedicine, String date, String observations) {
            this.idAlarmMedication = idAlarmMedication;
            this.medicationID = medicationID;
            this.consumedMedicine = consumedMedicine;
            this.date = date;
            this.observations = observations;
        }
    }


}
