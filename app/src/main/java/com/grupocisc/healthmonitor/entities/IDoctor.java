package com.grupocisc.healthmonitor.entities;

import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

//@DatabaseTable(tableName = "DoctorTable")
@DatabaseTable(tableName = Constantes.TABLA_DOCTOR)

public class IDoctor implements Serializable {

    @Getter
    @Setter
    @DatabaseField(generatedId=true)
    private int id;
    @Getter
    @Setter
    @DatabaseField
    private int doctorId;
    @Getter
    @Setter
    @DatabaseField
    private String Nombres;
    @Getter
    @Setter
    @DatabaseField
    private String Apellidos;
    @Getter
    @Setter
    @DatabaseField
    private String Mail;
    @Getter
    @Setter
    @DatabaseField
    private String phone;
    @Getter
    @Setter
    @DatabaseField
    private String especialidad;

}
