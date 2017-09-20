package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mariuxi on 01/03/2017.
 */

public interface IConculPeso {

    // <ArrayList<IRegistrePerson.RegistroPersona>>()

    @GET("procesos_oap/ejecuta_slc_peso_andr")
    Call<List<Objeto>> CunculPeso(@Query("email") String email,
                                  //@Query("parametro") String parametro,
                                  @Query("fechaDesde") String fechaDesde,
                                  @Query("fechaHasta") String fechaHasta
    );

    public class Objeto {
        //  List<Usuario> LsiataUsuario;

        String fecha;
        float peso;
        float imc;
        float porcentajeAgua;
        float porcentajeGrasa;
        float dmo;
        float masaMuscular;
        float tmb;
        //String medio;
        String observacion;


        public float getPeso() {
            return peso;
        }

        public void setPeso(float peso) {
            this.peso = peso;
        }

        public float getImc() {
            return imc;
        }

        public void setImc(float imc) {
            this.imc = imc;
        }

        public float getAgua() {
            return porcentajeAgua;
        }

        public void setAgua(float porcentajeAgua) {
            this.porcentajeAgua = porcentajeAgua;
        }

        public float getGrasa() {
            return porcentajeGrasa;
        }

        public void setGrasa(float grasa) {
            this.porcentajeGrasa = grasa;
        }

        public float getDmo() {
            return dmo;
        }

        public void setDmo(float dmo) {
            this.dmo = dmo;
        }

        public float getTmb() {
            return tmb;
        }

        public void setTmb(float tmb) {
            this.tmb = tmb;
        }

        public float getMasamuscular() {
            return masaMuscular;
        }

        public void setMasamuscular(float masaMuscular) {
            this.masaMuscular = masaMuscular;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }


        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

    }
}

