package com.grupocisc.healthmonitor.Home;

public class ItemMenu {
    public String title;
    public int iconRes;
    public int id;
    public int tipo; // 1 cursos 2 lista menu top

    public ItemMenu(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public ItemMenu(String title, int iconRes , int id , int tipo) {
        this.title = title;
        this.iconRes = iconRes;
        this.id = id;
        this.tipo = tipo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
