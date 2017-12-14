package com.grupocisc.healthmonitor.entities;

/**
 * Created by aonate on 12/06/2017.
 */
import com.grupocisc.healthmonitor.Utils.Constantes;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

//@DatabaseTable(tableName = "Hba1cTable")
@DatabaseTable(tableName = Constantes.TABLA_HBA1C)

public class IHba1c implements Serializable {
    @Getter
    @Setter
    @DatabaseField(generatedId = true)
    private int id;
    @Getter
    @Setter
    @DatabaseField
    private int idBdServer;
    @Getter
    @Setter
    @DatabaseField
    private float concentracion;
    @Getter
    @Setter
    @DatabaseField
    private float Cetonas;
    @Getter
    @Setter
    @DatabaseField
    private String fecha;
    @Getter
    @Setter
    @DatabaseField
    private String hora;
    @Getter
    @Setter
    @DatabaseField
    private String observacion;
    @Getter
    @Setter
    @DatabaseField
    private String enviadoServer;
    @Getter
    @Setter
    @DatabaseField
    private String operacion;


}
