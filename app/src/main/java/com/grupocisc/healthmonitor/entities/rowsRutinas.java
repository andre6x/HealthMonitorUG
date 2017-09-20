package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by GrupoCisc on 09/04/2015.
 */

@DatabaseTable(tableName = "rowsRutinasTable")
public class rowsRutinas {

    @DatabaseField
    private int idmed_courses;
    @DatabaseField
    private int idmed_categories;
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private String url_cdn_xsmall;
    @DatabaseField
    private String url_cdn_small;
    @DatabaseField
    private String url_cdn_medium;
    @DatabaseField
    private String url_cdn_large;
    @DatabaseField
    private String folder;
    @DatabaseField
    private String date_create;
    @DatabaseField
    private int status;
    @DatabaseField
    private int total_lesson;
    @DatabaseField
    private String start_date;
    @DatabaseField
    private String end_date;
    @DatabaseField
    private float progress;
    @DatabaseField
    private String subscribers;
    @DatabaseField
    private String ranking;


    public int getIdmed_courses() {
        return idmed_courses;
    }

    public void setIdmed_courses(int idmed_courses) {
        this.idmed_courses = idmed_courses;
    }

    public int getIdmed_categories() {
        return idmed_categories;
    }

    public void setIdmed_categories(int idmed_categories) {
        this.idmed_categories = idmed_categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_cdn_small() {
        return url_cdn_small;
    }

    public void setUrl_cdn_small(String url_cdn_small) {
        this.url_cdn_small = url_cdn_small;
    }
    ///////
    public String getUrl_cdn_xsmall() {
        return url_cdn_xsmall;
    }

    public void setUrl_cdn_xsmall(String url_cdn_xsmall) {
        this.url_cdn_xsmall = url_cdn_xsmall;
    }

    public String getUrl_cdn_medium() {
        return url_cdn_medium;
    }

    public void setUrl_cdn_medium(String url_cdn_medium) {
        this.url_cdn_medium = url_cdn_medium;
    }

    public String getUrl_cdn_large() {
        return url_cdn_large;
    }

    public void setUrl_cdn_large(String url_cdn_large) {
        this.url_cdn_large = url_cdn_large;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_lesson() {
        return total_lesson;
    }

    public void setTotal_lesson(int total_lesson) {
        this.total_lesson = total_lesson;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }




}
