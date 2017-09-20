package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */
public interface IV2RegisterMedication {

    @POST("diabetes/patientUsers/registerMedication")
    Call<MedicationRegister> RegisterMedication(@Body ObjRegisterMedication rPeso);
    class MedicationRegister{
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

    class ObjRegisterMedication {
        @Getter
        @Setter
        String  identifier ;            // 109jordy@gmail.com ,
        @Getter
        @Setter
        int     medicineID   ;          //10,
        @Getter
        @Setter
        int     doseMedicine ;          //6,
        @Getter
        @Setter
        String     frequencyType ;         // 0 ,
        @Getter
        @Setter
        int  times ;                 //4,
        @Getter
        @Setter
        String  frequencyDescription ; // F ,
        @Getter
        @Setter
        String  startDate ;             // 2017-08-10 ,
        @Getter
        @Setter
        String  endDate ;               // 2017-08-19 ,
        @Getter
        @Setter
        String  observations ;          // prueba registro medicacion

        public ObjRegisterMedication(String identifier, int medicineID, int doseMedicine, String frequencyType, int times, String frequencyDescription, String startDate, String endDate, String observations) {
            this.identifier = identifier;
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

    @POST("diabetes/patientUsers/updateMedication")
    Call<MedicationUpdate> UpdateMedication(@Body ObjUpadateMedication rPeso);
    class MedicationUpdate{
        @Getter
        @Setter
        int idCodResult ;           // 0
        @Getter
        @Setter
        String resultDescription;   // "Sus datos se registraron con exito"
    }

    class ObjUpadateMedication{
        @Getter
        @Setter
        String  identifier ;            // 109jordy@gmail.com ,
        @Getter
        @Setter
        int     medicationID;
        @Getter
        @Setter
        int     medicineID   ;          //10,
        @Getter
        @Setter
        int     doseMedicine ;          //6,
        @Getter
        @Setter
        String     frequencyType ;         // 0 ,
        @Getter
        @Setter
        int  times ;                 //4,
        @Getter
        @Setter
        String  frequencyDescription ; // F ,
        @Getter
        @Setter
        String  startDate ;             // 2017-08-10 ,
        @Getter
        @Setter
        String  endDate ;               // 2017-08-19 ,
        @Getter
        @Setter
        String  observations ;          // prueba registro medicacion

        public ObjUpadateMedication(String identifier, int medicationID, int medicineID, int doseMedicine, String frequencyType, int times, String frequencyDescription, String startDate, String endDate, String observations) {
            this.identifier = identifier;
            this.medicationID = medicationID;
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

}
