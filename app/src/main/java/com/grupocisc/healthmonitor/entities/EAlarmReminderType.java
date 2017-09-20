package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mpolo on 07/26/2017.
 */
@DatabaseTable(tableName = "AlarmReminderType")
public class EAlarmReminderType implements Comparable<EAlarmReminderType> , Serializable {

    @DatabaseField
    private String reminderTypeCode;
    @DatabaseField
    private String reminderTypeDescription;
    @DatabaseField
    private String reminderTypeStatus;

    @Override
    public String toString() {
        return "[ " +
                "reminderTypeCode = " + this.reminderTypeCode +
                " reminderTypeDescription = " + this.reminderTypeDescription +
                " reminderTypeStatus = " + this.reminderTypeStatus +
                " ]" ;
    }

    @Override
    public int compareTo(@NonNull EAlarmReminderType o) {
        return 0;
    }

    public String getReminderTypeCode() {
        return reminderTypeCode;
    }

    public void setReminderTypeCode(String reminderTypeCode) {
        this.reminderTypeCode = reminderTypeCode;
    }

    public String getReminderTypeDescription() {
        return reminderTypeDescription;
    }

    public void setReminderTypeDescription(String reminderTypeDescription) {
        this.reminderTypeDescription = reminderTypeDescription;
    }

    public String getReminderTypeStatus() {
        return reminderTypeStatus;
    }

    public void setReminderTypeStatus(String reminderTypeStatus) {
        this.reminderTypeStatus = reminderTypeStatus;
    }
}
