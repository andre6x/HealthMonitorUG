package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mpolo on 07/31/2017.
 */

@DatabaseTable(tableName = "EAlarmDetails")
public class EAlarmDetails implements Serializable {
    @DatabaseField(generatedId=true)
    private int alarmDetailId;
    //@DatabaseField
    //private int             alarmHeaderId;
    @DatabaseField
    private int registeredMedicinesId;
    @DatabaseField
    private String          alarmDetailDate;
    @DatabaseField
    private String          alarmDetailHour;
    @DatabaseField
    private String          alarmDetailStatus;

    @DatabaseField
    private String          alarmDetailCreateDate;


    //CAMPOS A CARGAR EN EL OBJETO PARA NO CONSULTAR LA BASE NUEVAMENTE.

    @DatabaseField
    private int         medicineCode;
    @DatabaseField
    private String      medicineDescription;



    @DatabaseField
    private String email;
    @DatabaseField
    private String idUsuario;
    @DatabaseField
    private String sentWs;
    @DatabaseField
    private String operationDb;
    @DatabaseField
    private int idServerDb;

    @Override
    public String toString(){
        return "[" +
                "alarmDetailId=" + alarmDetailId +
                ", registeredMedicinesId=" + registeredMedicinesId +
                ", alarmDetailHour=" + alarmDetailHour +
                ", alarmDetailStatus=" + alarmDetailStatus +
                ", alarmDetailCreateDate=" + alarmDetailCreateDate +
                ", medicineCode=" + medicineCode +
                ", medicineDescription=" + medicineDescription +
                ", email=" + email +
                ", idUsuario=" + idUsuario +
                ", sentWs=" + sentWs +
                ", operationDb=" + operationDb +
                ", idServerDb=" + idServerDb +
                "]";
    }



    public int getAlarmDetailId() {
        return alarmDetailId;
    }

    public void setAlarmDetailId(int alarmDetailId) {
        this.alarmDetailId = alarmDetailId;
    }

//    public int getAlarmHeaderId() {
//        return alarmHeaderId;
//    }
//
//    public void setAlarmHeaderId(int alarmHeaderId) {
//        this.alarmHeaderId = alarmHeaderId;
//    }

//    public String getAlarmDetailDate() {
//        return alarmDetailDate;
//    }
//
//    public void setAlarmDetailDate(String alarmDetailDate) {
//        this.alarmDetailDate = alarmDetailDate;
//    }

    public String getAlarmDetailHour() {
        return alarmDetailHour;
    }

    public void setAlarmDetailHour(String alarmDetailHour) {
        this.alarmDetailHour = alarmDetailHour;
    }

    public String getAlarmDetailStatus() {
        return alarmDetailStatus;
    }

    public void setAlarmDetailStatus(String alarmDetailStatus) {
        this.alarmDetailStatus = alarmDetailStatus;
    }

    public String getAlarmDetailCreateDate() {
        return alarmDetailCreateDate;
    }

    public void setAlarmDetailCreateDate(String alarmDetailCreateDate) {
        this.alarmDetailCreateDate = alarmDetailCreateDate;
    }

    public int getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(int medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getMedicineDescription() {
        return medicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        this.medicineDescription = medicineDescription;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSentWs() {
        return sentWs;
    }

    public void setSentWs(String sentWs) {
        this.sentWs = sentWs;
    }

    public String getOperationDb() {
        return operationDb;
    }

    public void setOperationDb(String operationDb) {
        this.operationDb = operationDb;
    }

    public int getIdServerDb() {
        return idServerDb;
    }

    public void setIdServerDb(int idServerDb) {
        this.idServerDb = idServerDb;
    }

    public int getRegisteredMedicinesId() {
        return registeredMedicinesId;
    }

    public void setRegisteredMedicinesId(int registeredMedicinesId) {
        this.registeredMedicinesId = registeredMedicinesId;
    }

    public String getAlarmDetailDate() {
        return alarmDetailDate;
    }

    public void setAlarmDetailDate(String alarmDetailDate) {
        this.alarmDetailDate = alarmDetailDate;
    }
}