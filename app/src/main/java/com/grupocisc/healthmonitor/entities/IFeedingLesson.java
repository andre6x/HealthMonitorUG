package com.grupocisc.healthmonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Walter on 13/02/2017.
 */

public interface IFeedingLesson {

    @GET("getLesson")
    Call<FeedingLesson> getLessonFrom(@Query("cod_servicio") String CountryCode,
                                      @Query("course") String course,
                                      @Query("limit") String limit,
                                      @Query("offset") String offset);

    @DatabaseTable(tableName = "FeedingLessonTable")
    public class FeedingLesson{
        private List<rowsFeedingLesson> rows;
        private summary summary;
        @DatabaseField
        private int code;

        public List<rowsFeedingLesson> getRows() {
            return rows;
        }

        public void setRows(List<rowsFeedingLesson> rows) {
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
