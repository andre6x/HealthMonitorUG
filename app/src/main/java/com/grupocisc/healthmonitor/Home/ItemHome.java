package com.grupocisc.healthmonitor.Home;

import lombok.Getter;
import lombok.Setter;

public class ItemHome {
    @Getter
    @Setter
    public String title;
    @Getter
    @Setter
    public int iconRes;
    @Getter
    @Setter
    public int imageRes;
    @Getter
    @Setter
    public int id;
    @Getter
    @Setter
    public String color;
    @Getter
    @Setter
    public String tipo;


    public ItemHome(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public ItemHome(String title, int iconRes , int id ) {
        this.title = title;
        this.iconRes = iconRes;
        this.id = id;
    }

    public ItemHome(String title, int iconRes, int id, String color) {
        this.title = title;
        this.iconRes = iconRes;
        this.id = id;
        this.color = color;
    }

    public ItemHome(String title, int iconRes, int imageRes, int id, String color, String tipo) {
        this.title = title;
        this.iconRes = iconRes;
        this.imageRes = imageRes;
        this.id = id;
        this.color = color;
        this.tipo = tipo;
    }

}
