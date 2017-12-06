package com.grupocisc.healthmonitor.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.POST;

public interface IPaises {

    @POST("controlServices/diabetes/login/queryCountries")
    Call<Paises> getFromPaises();

    class Paises{
        @Getter
        @Setter
        int idCodResult;
        @Getter
        @Setter
        String resultDescription;
        @Getter
        @Setter
        List<Rows> rows ;

    }

    class Rows{
        @Getter
        @Setter
        int countryId;
        @Getter
        @Setter
        private String name;
    }
    /*
    {
    "idCodResult": 0,
    "resultDescription": "La consulta se ejecuto exitosamente",
    "rows": [
        {
            "countryId": 1,
            "name": "ECUADOR"
        },
        {
            "countryId": 2,
            "name": "COLOMBIA"
        },
        {
            "countryId": 3,
            "name": "ESPAÑA"
        },
        {
            "countryId": 4,
            "name": "PERU"
        },
        {
            "countryId": 5,
            "name": "RUSIA"
        },
        {
            "countryId": 6,
            "name": "ESTADOS UNIDOS"
        }
    ]
}
    *
    *
    * */

}
