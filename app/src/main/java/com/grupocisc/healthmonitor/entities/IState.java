
package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by aonate on 12/01/2017.
 */

@DatabaseTable(tableName = "StateTable")

public class IState implements Serializable {

    @Getter
    @Setter
    @DatabaseField(generatedId=true)
    private int id;
    @Getter
    @Setter
    @DatabaseField
    private int idBdServer;
    @Getter
    @Setter
    @DatabaseField
    private int IdStatus;
    @Getter
    @Setter
    @DatabaseField
    private String StatusName;
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
    private String enviadoServer ;
    @Getter
    @Setter
    @DatabaseField
    private String operationDb;


}
