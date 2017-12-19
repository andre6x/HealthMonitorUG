package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mpolo on 07/26/2017.
 */
//@DatabaseTable(tableName = "AlarmReminderTime")
@DatabaseTable(tableName = Constantes.TABLA_ALARM_REMINDER_TIME)

public class EAlarmReminderTime implements Comparable<EAlarmReminderTime> , Serializable {
    @DatabaseField
    private String reminderTypeCode;
    @DatabaseField
    private int reminderTimeCode;
    @DatabaseField
    private String reminderTimeDescription;
    @DatabaseField
    private String reminderTimeStatus;

    @Override
    public String toString() {
        return "[ " +
                "reminderTypeCode = " + this.reminderTypeCode +
                " reminderTimeCode = " + this.reminderTimeCode +
                " reminderTimeDescription = " + this.reminderTimeDescription +
                " ]" ;
    }

    @Override
    public int compareTo(@NonNull EAlarmReminderTime o) {
        return 0;
    }

    public String getReminderTypeCode() {
        return reminderTypeCode;
    }

    public void setReminderTypeCode(String reminderTypeCode) {
        this.reminderTypeCode = reminderTypeCode;
    }

    public int getReminderTimeCode() {
        return reminderTimeCode;
    }

    public void setReminderTimeCode(int reminderTimeCode) {
        this.reminderTimeCode = reminderTimeCode;
    }

    public String getReminderTimeDescription() {
        return reminderTimeDescription;
    }

    public void setReminderTimeDescription(String reminderTimeDescription) {
        this.reminderTimeDescription = reminderTimeDescription;
    }

    public String getReminderTimeStatus() {
        return reminderTimeStatus;
    }

    public void setReminderTimeStatus(String reminderTimeStatus) {
        this.reminderTimeStatus = reminderTimeStatus;
    }
}
