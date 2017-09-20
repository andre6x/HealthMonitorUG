package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by GrupoLink on 09/04/2015.
 */

public interface IRutinas {

    @GET("getCourses")
    Call<Rutinas> getNewsFrom(@Query("cod_servicio") String CountryCode,
                              @Query("limit") String limit,
                              @Query("offset") String offset);

    @DatabaseTable(tableName = "RutinasTable")
    public class Rutinas{

        private List<rowsRutinas> rows;
        private summary summary;
        private int code;



        public List<rowsRutinas> getRows() {
            return rows;
        }

        public void setRows(List<rowsRutinas> rows) {
            this.rows = rows;
        }

        public summary getSummary() {
            return summary;
        }

        public void setSummary(summary summary) {
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
