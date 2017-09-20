package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mpolo on 07/26/2017.
 */

@DatabaseTable(tableName = "MedicineType")
public class EMedicineType implements Comparable <EMedicineType> , Serializable {
    @DatabaseField
    private int medicineTypeCode;
    @DatabaseField
    private String medicineTypeDescription;
    @DatabaseField
    private String medicineTypeStatus;

    @Override
    public String toString() {
        return "[ " +
                "medicineTypeCode = " + this.getMedicineTypeCode() +
                " medicineTypeDescription = " + this.getMedicineTypeDescription() +
                " medicineTypeStatus = " + this.getMedicineTypeStatus() +
                " ]" ;
    }

    @Override
    public int compareTo(@NonNull EMedicineType o) {
        String a  = this.getMedicineTypeDescription();
        String b  = o.getMedicineTypeDescription();
        return a.compareTo(b);
    }

    public int getMedicineTypeCode() {
        return medicineTypeCode;
    }

    public void setMedicineTypeCode(int medicineTypeCode) {
        this.medicineTypeCode = medicineTypeCode;
    }

    public String getMedicineTypeDescription() {
        return medicineTypeDescription;
    }

    public void setMedicineTypeDescription(String medicineTypeDescription) {
        this.medicineTypeDescription = medicineTypeDescription;
    }

    public String getMedicineTypeStatus() {
        return medicineTypeStatus;
    }

    public void setMedicineTypeStatus(String medicineTypeStatus) {
        this.medicineTypeStatus = medicineTypeStatus;
    }

}
