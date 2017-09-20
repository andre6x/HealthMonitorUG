package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mpolo on 08/11/2017.
 */

@DatabaseTable(tableName = "EAlarmTakeMedicine")
public class EAlarmTakeMedicine implements Comparable <EAlarmTakeMedicine>, Serializable {
    @DatabaseField(generatedId=true)
    private int alarmTakeMedicineId;
    @DatabaseField
    private int alarmDetailId;
    @DatabaseField
    private int registeredMedicinesId;

    @DatabaseField
    private String alarmDetailHour;
    @DatabaseField
    private String alarmTakeMedicineDate;

    @DatabaseField
    private String alarmTakeMedicineStatus;

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


    @DatabaseField
    private int alarmDetailIdServerDB;
    @DatabaseField
    private int registeredMedicinesIdServerDB;


    public int getAlarmTakeMedicineId() {
        return alarmTakeMedicineId;
    }

    public void setAlarmTakeMedicineId(int alarmTakeMedicineId) {
        this.alarmTakeMedicineId = alarmTakeMedicineId;
    }

    public String getAlarmTakeMedicineDate() {
        return alarmTakeMedicineDate;
    }

    public void setAlarmTakeMedicineDate(String alarmTakeMedicineDate) {
        this.alarmTakeMedicineDate = alarmTakeMedicineDate;
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

    @Override
    public String toString(){
        return "[" +
                "alarmTakeMedicineId=" + this.alarmTakeMedicineId +
                ",alarmDetailId=" +  this.alarmDetailId +
                ",registeredMedicinesId="+ this.registeredMedicinesId +

                ",alarmDetailHour=" +  this.alarmDetailHour +
                ",alarmTakeMedicineDate=" +  this.alarmTakeMedicineDate +
                ",alarmTakeMedicineStatus=" +  this.alarmTakeMedicineStatus +
                "]";

    }

    @Override
    public int compareTo(@NonNull EAlarmTakeMedicine eAlarmTakeMedicine) {
        String a = this.getAlarmTakeMedicineId()+"";
        String b = eAlarmTakeMedicine.getAlarmTakeMedicineId()+"";
        return a.compareTo(b);
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

    public String getAlarmTakeMedicineStatus() {
        return alarmTakeMedicineStatus;
    }

    public void setAlarmTakeMedicineStatus(String alarmTakeMedicineStatus) {
        this.alarmTakeMedicineStatus = alarmTakeMedicineStatus;
    }

    public String getAlarmDetailHour() {
        return alarmDetailHour;
    }

    public void setAlarmDetailHour(String alarmDetailHour) {
        this.alarmDetailHour = alarmDetailHour;
    }

    public int getRegisteredMedicinesId() {
        return registeredMedicinesId;
    }

    public void setRegisteredMedicinesId(int registeredMedicinesId) {
        this.registeredMedicinesId = registeredMedicinesId;
    }

    public int getAlarmDetailId() {
        return alarmDetailId;
    }

    public void setAlarmDetailId(int alarmDetailId) {
        this.alarmDetailId = alarmDetailId;
    }

    public int getAlarmDetailIdServerDB() {
        return alarmDetailIdServerDB;
    }

    public void setAlarmDetailIdServerDB(int alarmDetailIdServerDB) {
        this.alarmDetailIdServerDB = alarmDetailIdServerDB;
    }

    public int getRegisteredMedicinesIdServerDB() {
        return registeredMedicinesIdServerDB;
    }

    public void setRegisteredMedicinesIdServerDB(int registeredMedicinesIdServerDB) {
        this.registeredMedicinesIdServerDB = registeredMedicinesIdServerDB;
    }
}
