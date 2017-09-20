package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 16/04/2015.
 */
public interface ItopTen {

    @GET("getToptenCourses")
    Call<TopTen> getTopTen(@Query("ranking") String ranking,
                           @Query("cod_servicio") String CountryCode);



    public class TopTen{
        private summary summary;
        private List<rowsTopTen> rows;
        private int code;

        public com.grupocisc.healthmonitor.entities.summary getSummary() {
            return summary;
        }

        public void setSummary(com.grupocisc.healthmonitor.entities.summary summary) {
            this.summary = summary;
        }

        public List<rowsTopTen> getRows() {
            return rows;
        }

        public void setRows(List<rowsTopTen> rows) {
            this.rows = rows;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
