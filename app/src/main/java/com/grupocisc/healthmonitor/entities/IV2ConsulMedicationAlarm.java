package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */
public interface IV2ConsulMedicationAlarm {
    
    @POST("diabetes/patientUsers/queryMedicationAlarmLog")
    Call<Obj> queryMedicationAlarmLog(@Body ObjQueryMedicationAlarmLog obj);
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

    class Rows{
        @Getter
        @Setter
        int         idRegisterDB; //": 154,
        @Getter
        @Setter
        int         medicineID; //10,
        @Getter
        @Setter
        int      doseMedicine; //6,
        @Getter
        @Setter
        String         frequencyType; //": "0",
        @Getter
        @Setter
        int         times; //": 4,
        @Getter
        @Setter
        String      frequencyDescription; //": "F",
        @Getter
        @Setter
        String      startDate; //": "2017-08-10 00:00:00.0",
        @Getter
        @Setter
        String      endDate; //": "2017-08-19 00:00:00.0",
        @Getter
        @Setter
        String      observations; //": "PRUEBA REGISTRO MEDICACION",
        @Getter
        @Setter
        List<Alarms> alarmas;

        public Rows(){
        }

        public Rows(int idRegisterDB, int medicineID, int doseMedicine, String frequencyType, int times, String frequencyDescription, String startDate, String endDate, String observations) {
            this.idRegisterDB = idRegisterDB;
            this.medicineID = medicineID;
            this.doseMedicine = doseMedicine;
            this.frequencyType = frequencyType;
            this.times = times;
            this.frequencyDescription = frequencyDescription;
            this.startDate = startDate;
            this.endDate = endDate;
            this.observations = observations;
        }

    }

    class Alarms{
        @Getter
        @Setter
        int idRegisterDB ;//": 69,
        @Getter
        @Setter
        String        medicine;//\": 69,": "Ondansetrón 4 mg ",
        @Getter
        @Setter
        String        hourAlarm;//\": 69,": "08:00:00"

        public Alarms(){
        }

        public Alarms(int idRegisterDB, String medicine, String hourAlarm) {
            this.idRegisterDB = idRegisterDB;
            this.medicine = medicine;
            this.hourAlarm = hourAlarm;
        }

    }

    class ObjQueryMedicationAlarmLog {
        @Getter
        @Setter
        String      identifier ;            // 109jordy@gmail.com ,

        public ObjQueryMedicationAlarmLog(String identifier) {
            this.identifier = identifier;
        }
    }

}
