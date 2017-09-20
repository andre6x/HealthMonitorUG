package com.grupocisc.healthmonitor.entities;

/**
 * Created by GrupoLink on 28/04/2015.
 */
public class postParamsFavoritesCategory {

    private int categoryID;
    private int status;

    public postParamsFavoritesCategory() {
    }

    public postParamsFavoritesCategory(int categoryID, int status) {
        this.categoryID = categoryID;
        this.status = status;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
