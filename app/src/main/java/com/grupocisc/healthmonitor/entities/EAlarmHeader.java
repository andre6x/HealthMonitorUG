package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mpolo on 07/31/2017.
 */

@DatabaseTable(tableName = "EAlarmHeader")
public class EAlarmHeader {
    @DatabaseField(generatedId=true)
    private int alarmHeaderId;
    @DatabaseField
    private int         medicineCode;
    @DatabaseField
    private String      medicineDescription;
    @DatabaseField
    private int         alarmHeaderReminderTypeCode;
    @DatabaseField
    private String      alarmHeaderReminderTypeDescription;
    @DatabaseField
    private int         alarmHeaderReminderTimeCode;
    @DatabaseField
    private String      alarmHeaderReminderTimeDescription;
    @DatabaseField
    private int         alarmHeaderReminderDuration; // 0=Continuo or 5=5 días
    @DatabaseField
    private String      alarmHeaderReminderDays; // ALL or MON|TUE|WED or MON|FRI

    @DatabaseField
    private String      alarmHeaderReminderStartHour; //
    @DatabaseField
    private String      alarmHeaderReminderStartDate; //
    @DatabaseField
    private String      alarmHeaderReminderEndDate; // NULL or Date

    @DatabaseField
    private String      alarmHeaderReminderObservation; // some description

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


    public String getOperationDb() {
        return operationDb;
    }

    public void setOperationDb(String operationDb) {
        this.operationDb = operationDb;
    }

    public int getAlarmHeaderId() {
        return alarmHeaderId;
    }

    public void setAlarmHeaderId(int alarmHeaderId) {
        this.alarmHeaderId = alarmHeaderId;
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

    public int getAlarmHeaderReminderTypeCode() {
        return alarmHeaderReminderTypeCode;
    }

    public void setAlarmHeaderReminderTypeCode(int alarmHeaderReminderTypeCode) {
        this.alarmHeaderReminderTypeCode = alarmHeaderReminderTypeCode;
    }

    public String getAlarmHeaderReminderTypeDescription() {
        return alarmHeaderReminderTypeDescription;
    }

    public void setAlarmHeaderReminderTypeDescription(String alarmHeaderReminderTypeDescription) {
        this.alarmHeaderReminderTypeDescription = alarmHeaderReminderTypeDescription;
    }

    public int getAlarmHeaderReminderTimeCode() {
        return alarmHeaderReminderTimeCode;
    }

    public void setAlarmHeaderReminderTimeCode(int alarmHeaderReminderTimeCode) {
        this.alarmHeaderReminderTimeCode = alarmHeaderReminderTimeCode;
    }

    public String getAlarmHeaderReminderTimeDescription() {
        return alarmHeaderReminderTimeDescription;
    }

    public void setAlarmHeaderReminderTimeDescription(String alarmHeaderReminderTimeDescription) {
        this.alarmHeaderReminderTimeDescription = alarmHeaderReminderTimeDescription;
    }

    public int getAlarmHeaderReminderDuration() {
        return alarmHeaderReminderDuration;
    }

    public void setAlarmHeaderReminderDuration(int alarmHeaderReminderDuration) {
        this.alarmHeaderReminderDuration = alarmHeaderReminderDuration;
    }

    public String getAlarmHeaderReminderDays() {
        return alarmHeaderReminderDays;
    }

    public void setAlarmHeaderReminderDays(String alarmHeaderReminderDays) {
        this.alarmHeaderReminderDays = alarmHeaderReminderDays;
    }

    public String getAlarmHeaderReminderStartHour() {
        return alarmHeaderReminderStartHour;
    }

    public void setAlarmHeaderReminderStartHour(String alarmHeaderReminderStartHour) {
        this.alarmHeaderReminderStartHour = alarmHeaderReminderStartHour;
    }

    public String getAlarmHeaderReminderStartDate() {
        return alarmHeaderReminderStartDate;
    }

    public void setAlarmHeaderReminderStartDate(String alarmHeaderReminderStartDate) {
        this.alarmHeaderReminderStartDate = alarmHeaderReminderStartDate;
    }

    public String getAlarmHeaderReminderEndDate() {
        return alarmHeaderReminderEndDate;
    }

    public void setAlarmHeaderReminderEndDate(String alarmHeaderReminderEndDate) {
        this.alarmHeaderReminderEndDate = alarmHeaderReminderEndDate;
    }

    public String getAlarmHeaderReminderObservation() {
        return alarmHeaderReminderObservation;
    }

    public void setAlarmHeaderReminderObservation(String alarmHeaderReminderObservation) {
        this.alarmHeaderReminderObservation = alarmHeaderReminderObservation;
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

    public int getIdServerDb() {
        return idServerDb;
    }

    public void setIdServerDb(int idServerDb) {
        this.idServerDb = idServerDb;
    }
}
