package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Walter on 13/02/2017.
 */

@DatabaseTable(tableName = "rowsFeedingLessonTable")
public class rowsFeedingLesson {

    @DatabaseField
    private int idmed_lesson;
    @DatabaseField
    private int idmed_courses;
    @DatabaseField
    private String topic;
    @DatabaseField
    private String description;
    @DatabaseField
    private int has_quiz_mandatory;
    @DatabaseField
    private int max_question_show;
    @DatabaseField
    private int min_to_approve;
    @DatabaseField
    private String folder;
    @DatabaseField
    private String file;
    @DatabaseField
    private String date_created;
    @DatabaseField
    private int status_lesson;
    @DatabaseField
    private float current_status;
    @DatabaseField
    private float progress;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHas_quiz_mandatory() {
        return has_quiz_mandatory;
    }

    public void setHas_quiz_mandatory(int has_quiz_mandatory) {
        this.has_quiz_mandatory = has_quiz_mandatory;
    }

    public int getMax_question_show() {
        return max_question_show;
    }

    public void setMax_question_show(int max_question_show) {
        this.max_question_show = max_question_show;
    }

    public int getMin_to_approve() {
        return min_to_approve;
    }

    public void setMin_to_approve(int min_to_approve) {
        this.min_to_approve = min_to_approve;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getStatus_lesson() {
        return status_lesson;
    }

    public void setStatus_lesson(int status_lesson) {
        this.status_lesson = status_lesson;
    }

    public float getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(float current_status) {
        this.current_status = current_status;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
