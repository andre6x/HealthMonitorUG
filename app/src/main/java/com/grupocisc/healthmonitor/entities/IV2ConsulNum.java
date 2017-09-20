package com.grupocisc.healthmonitor.entities;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 10/04/2015.
 */
public interface IV2ConsulNum {

    @POST("diabetes/login/validateRegister")
    Call<ConsulNum> ConsultaNumero(@Body ObjUserNum rPeso);
    class ConsulNum{
        @Getter
        @Setter
        int idCodResult ;
        @Getter
        @Setter
        String resultDescription;
    }

    /**/
    class ObjUserNum{
        @Getter
        @Setter
        String email;
        @Getter
        @Setter
        String cellPhone;
        public ObjUserNum(String email,String cellPhone){
            this.email = email;
            this.cellPhone = cellPhone;
        }
    }

}
