package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 10/02/2017.
 */

public interface IDiet {

    @GET("getCourses")
    Call<IDiet.Diet> getNewsFrom(@Query("cod_servicio") String CountryCode,
                                 @Query("limit") String limit,
                                 @Query("offset") String offset);

    @DatabaseTable(tableName = "DietTable")
    public class Diet {

        private List<rowsDiets> rows;
        private summary summary;
        private int code;

        public List<rowsDiets> getRows() {
            return rows;
        }

        public void setRows(List<rowsDiets> rows) {
            this.rows = rows;
        }

        public com.grupocisc.healthmonitor.entities.summary getSummary() {
            return summary;
        }

        public void setSummary(com.grupocisc.healthmonitor.entities.summary summary) {
            this.summary = summary;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
