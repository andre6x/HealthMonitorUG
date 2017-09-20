package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Walter on 17/01/2017.
 */

@DatabaseTable(tableName = "rowsCheckLessonTable")
public class IRoutineCheckLesson {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int idmed_lesson;
    @DatabaseField
    private int idmed_courses;
    @DatabaseField
    private int check;
    @DatabaseField
    private String date_row;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdmed_lesson() {
        return idmed_lesson;
    }

    public void setIdmed_lesson(int idmed_lesson) {
        this.idmed_lesson = idmed_lesson;
    }

    public int getIdmed_courses() {
        return idmed_courses;
    }

    public void setIdmed_courses(int idmed_courses) {
        this.idmed_courses = idmed_courses;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getDate_row() {
        return date_row;
    }

    public void setDate_row(String date_row) {
        this.date_row = date_row;
    }
}
