package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */
public interface IV2RegisterAlarmMedication {
    
    @POST("diabetes/patientUsers/registerAlarmMedication")
    Call<registerAlarmMedication> registerAlarmMedication(@Body ObjRegisterAlarmMedication obj);
    class registerAlarmMedication{
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

    class ObjRegisterAlarmMedication {
        @Getter
        @Setter
        String      identifier ;            // 109jordy@gmail.com ,
        @Getter
        @Setter
        int         idMedication   ;          //154,
        @Getter
        @Setter
        String      hourAlarm ;          //08,
        @Getter
        @Setter
        String      date ;         // 0 ,
        @Getter
        @Setter
        String      observations ;          // prueba registro medicacion

        public ObjRegisterAlarmMedication(String identifier, int idMedication, String hourAlarm, String date, String observations) {
            this.identifier = identifier;
            this.idMedication = idMedication;
            this.hourAlarm = hourAlarm;
            this.date = date;
            this.observations = observations;
        }
    }


}
