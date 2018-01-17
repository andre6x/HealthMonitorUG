package com.grupocisc.healthmonitor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EAlarmReminderTime;
import com.grupocisc.healthmonitor.entities.EAlarmReminderType;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.EMedicineType;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.EMedicineUser;
import com.grupocisc.healthmonitor.entities.IDisease;
import com.grupocisc.healthmonitor.entities.IDoctor;
import com.grupocisc.healthmonitor.entities.IFeeding;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IKetone;
import com.grupocisc.healthmonitor.entities.IMedicines;
import com.grupocisc.healthmonitor.entities.INotifcationsMedical;
import com.grupocisc.healthmonitor.entities.IPressure;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.IRoutineCheckLesson;
import com.grupocisc.healthmonitor.entities.IState;
import com.grupocisc.healthmonitor.entities.ITrigliceros;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

public class Database extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "healthmonitorDB";
    private static final int DATABASE_VERSION = 4; // version 2 offline
    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            this.db = db;
            Log.i(Database.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, IGlucose.class);
            TableUtils.createTable(connectionSource, INotifcationsMedical.class);
            TableUtils.createTable(connectionSource, IWeight.class);
            TableUtils.createTable(connectionSource, IPressure.class);
            TableUtils.createTable(connectionSource, IState.class);
            TableUtils.createTable(connectionSource, EInsulin.class);
            TableUtils.createTable(connectionSource, IMedicines.class);
            TableUtils.createTable(connectionSource, EMedicine.class);
            TableUtils.createTable(connectionSource, EMedicineUser.class);
            TableUtils.createTable(connectionSource, IRegisteredMedicines.class);
            TableUtils.createTable(connectionSource, IPulse.class);
            TableUtils.createTable(connectionSource, IRoutineCheckLesson.class);
            TableUtils.createTable(connectionSource, IDisease.class);
            TableUtils.createTable(connectionSource, IDoctor.class);
            TableUtils.createTable(connectionSource, IFeeding.class);
            TableUtils.createTable(connectionSource, IColesterol.class);
            TableUtils.createTable(connectionSource, ITrigliceros.class);
            TableUtils.createTable(connectionSource, IHba1c.class);
            TableUtils.createTable(connectionSource, IKetone.class);
			TableUtils.createTable(connectionSource, EMedicineType.class);
            TableUtils.createTable(connectionSource, EAlarmReminderType.class);
            TableUtils.createTable(connectionSource, EAlarmReminderTime.class);
            TableUtils.createTable(connectionSource, EAlarmDetails.class);
            TableUtils.createTable(connectionSource, EAlarmTakeMedicine.class);
            TableUtils.createTable(connectionSource, IAsthma.class);


        } catch (SQLException e) {
            Log.e(Database.class.getName(), "ERROR AL CREAR LA BASE DE DATOS", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(Database.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, IGlucose.class, true);
            TableUtils.dropTable(connectionSource, INotifcationsMedical.class, true);
            TableUtils.dropTable(connectionSource, IWeight.class, true);
            TableUtils.dropTable(connectionSource, IPressure.class, true);
            TableUtils.dropTable(connectionSource, IState.class, true);
            TableUtils.dropTable(connectionSource, EInsulin.class, true);
            TableUtils.dropTable(connectionSource, EMedicine.class, true);
            TableUtils.dropTable(connectionSource, EMedicineUser.class, true);
            TableUtils.dropTable(connectionSource, IMedicines.class, true);
            TableUtils.dropTable(connectionSource, IRegisteredMedicines.class, true);
            TableUtils.dropTable(connectionSource, IPulse.class, true);
            TableUtils.dropTable(connectionSource, IRoutineCheckLesson.class, true);
            TableUtils.dropTable(connectionSource, IDisease.class,true);
            TableUtils.dropTable(connectionSource, IDoctor.class,true);
            TableUtils.dropTable(connectionSource, IFeeding.class, true);
            TableUtils.dropTable(connectionSource, IColesterol.class, true);
            TableUtils.dropTable(connectionSource, ITrigliceros.class, true);
            TableUtils.dropTable(connectionSource, IHba1c.class, true);
            TableUtils.dropTable(connectionSource, IKetone.class, true);
 			TableUtils.dropTable(connectionSource, EMedicineType.class, true);
            TableUtils.dropTable(connectionSource, EAlarmReminderType.class, true);
            TableUtils.dropTable(connectionSource, EAlarmReminderTime.class, true);
            TableUtils.dropTable(connectionSource, EAlarmDetails.class, true);
            TableUtils.dropTable(connectionSource, EAlarmTakeMedicine.class, true);
            TableUtils.dropTable(connectionSource, IAsthma.class,true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(Database.class.getName(), "ERROR AL DROP DE LA BASE DE DATOS", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
    }


    //------------------------inicio Limpiar Tablas----------------------------//

    public void CleanGlucose() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IGlucose.class);
    }

    public void CleanNotifcationsMedical() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), INotifcationsMedical.class);
    }
    public void CleanWeight() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IWeight.class);
    }
    public void CleanPressure() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IPressure.class);
    }
    public void CleanState() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IState.class);
    }

    public void cleanFeeding() throws SQLException{
        TableUtils.clearTable(getConnectionSource(), IFeeding.class);
    }

    public void CleanInsulin() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), EInsulin.class);
    }
    public void CleanMedicines() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IMedicines.class);
    }

    public void CleanMedicine() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), EMedicine.class);
    }

    public void CleanPulse() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IState.class);
    }
    public void CleanRegisteredMedicines() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IRegisteredMedicines.class);
    }

    public void CleanRegisteredDisease() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), IRegisteredMedicines.class);
    }
    public void CleanRoutineCheckLesson() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IRoutineCheckLesson.class);
    }

    public void CleanColesterol() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IRoutineCheckLesson.class);
    }

    public void CleanTrigliceros() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IRoutineCheckLesson.class);
    }
    public void CleanHba1c() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IRoutineCheckLesson.class);
    }
    public void CleanKetone() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IRoutineCheckLesson.class);
    }
   public void CleanEMedicineType() throws SQLException{
        TableUtils.createTable(getConnectionSource(), EMedicineType.class);
    }
    public void CleanEAlarmReminderType() throws SQLException{
        TableUtils.createTable(getConnectionSource(), EAlarmReminderType.class);
    }
    public void CleanEAlarmReminderTime() throws SQLException{
        TableUtils.createTable(getConnectionSource(), EAlarmReminderTime.class);
    }
    public void CleanEAlarmDetails() throws SQLException{
        TableUtils.createTable(getConnectionSource(), EAlarmDetails.class);
    }
    public void CleanEAlarmTakeMedicine() throws SQLException{
        TableUtils.createTable(getConnectionSource(), EAlarmTakeMedicine.class);
    }
    public void CleanAsthma() throws SQLException{
        TableUtils.createTable(getConnectionSource(), IAsthma.class);
    }

   /* public void CleanWidgetMatch() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), rowsWidgetMatch.class);
    }

    public void CleanMediaItems() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), rowsMediaItems.class);
    }*/

    //------------------------fin Limpiar Tablas----------------------------//

    public void BeginTransaction() {
        db.beginTransaction();
    }

    public void EndTransactionSuccess() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void EndTransaction() {
        db.endTransaction();
    }

}