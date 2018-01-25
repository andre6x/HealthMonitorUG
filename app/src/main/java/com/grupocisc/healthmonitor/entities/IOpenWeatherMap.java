package com.grupocisc.healthmonitor.entities;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by alex on 1/24/18.
 */

public interface IOpenWeatherMap {

    @POST("data/2.5/weather")
    Call<OpenWeatherMap> getData(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

    class OpenWeatherMap{
        @Getter
        @Setter
        Coordinates coord;

        @Getter
        @Setter
        List<Weather> weather;

        @Getter
        @Setter
        String base;

        @Getter
        @Setter
        Main main;

        @Getter
        @Setter
        int visibility;

        @Getter
        @Setter
        Wind wind;

        @Getter
        @Setter
        Cloud clouds;

        @Getter
        @Setter
        int dt;

        @Getter
        @Setter
        Sys sys;

        @Getter
        @Setter
        int id;

        @Getter
        @Setter
        String name;

        @Getter
        @Setter
        int cod;
    }

    class Coordinates{
        @Getter
        @Setter
        double lon;
        double lat;
    }

    class Weather{
        @Getter
        @Setter
        int id;

        @Getter
        @Setter
        String main;

        @Getter
        @Setter
        String description;

        @Getter
        @Setter
        String icon;
    }

    class Main{
        @Getter
        @Setter
        double temp;

        @Getter
        @Setter
        double pressure;

        @Getter
        @Setter
        int humidity;

        @Getter
        @Setter
        double temp_min;

        @Getter
        @Setter
        double temp_max;
    }

    class Wind{
        @Getter
        @Setter
        double speed;

        @Getter
        @Setter
        double deg;
    }

    class Cloud{
        @Getter
        @Setter
        int all;
    }

    class Sys{
        @Getter
        @Setter
        int type;

        @Getter
        @Setter
        int id;

        @Getter
        @Setter
        double message;

        @Getter
        @Setter
        String country;

        @Getter
        @Setter
        int sunrise;

        @Getter
        @Setter
        int sunset;
    }
}
