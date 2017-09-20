package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by User on 16/04/2015.
 */

@DatabaseTable(tableName = "TopTenTable")
public class rowsTopTen {

    @DatabaseField
    private String folder;
    @DatabaseField
    private String idmed_categories;
    @DatabaseField
    private String title;
    @DatabaseField
    private String idmed_courses;
    @DatabaseField
    private String name;
    @DatabaseField
    private String ranking;
    @DatabaseField
    private String subscribers;
    private String url_cdn_large;
    private String url_cdn_medium;
    @DatabaseField
    private String url_cdn_small;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
    public String getIdmed_categories() {
        return idmed_categories;
    }

    public void setIdmed_categories(String idmed_categories) {
        this.idmed_categories = idmed_categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdmed_courses() {
        return idmed_courses;
    }

    public void setIdmed_courses(String idmed_courses) {
        this.idmed_courses = idmed_courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getUrl_cdn_large() {
        return url_cdn_large;
    }

    public void setUrl_cdn_large(String url_cdn_large) {
        this.url_cdn_large = url_cdn_large;
    }

    public String getUrl_cdn_medium() {
        return url_cdn_medium;
    }

    public void setUrl_cdn_medium(String url_cdn_medium) {
        this.url_cdn_medium = url_cdn_medium;
    }

    public String getUrl_cdn_small() {
        return url_cdn_small;
    }

    public void setUrl_cdn_small(String url_cdn_small) {
        this.url_cdn_small = url_cdn_small;
    }
}
