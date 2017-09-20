package com.grupocisc.healthmonitor.entities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 10/04/2015.
 */
public interface IRegCrtPacient {

    @POST("diabetes/patientUsers/registerInsulin")
    Call<RegCrtPacient> setSendInsulinFrom(@Body rowInsulin rInsulin);

    @POST("/controlServices/diabetes/patientUsers/updateInsulin")
    Call<RegCrtPacient> setSendInsulinUpdateFrom(@Body rowInsulinUpdate rInsulin);


    @GET("personas/ejecuta_ctrlPAciente")
    Call<RegCrtPacient> RegCrtPacient(     @Query("email") String email,
                                           @Query("glucosa") float glucosa,
                                           @Query("insulina") float insulina,
                                           @Query("hba1c") float hba1c,
                                           @Query("cetona") float cetona,
                                           @Query("estatura") float estatura,
                                           @Query("colesterol") float colesterol,
                                           @Query("trigliceridos") float trigliceridos,
                                           @Query("hdl") float hdl,
                                           @Query("ldl") float ldl,
                                           @Query("presionSistolica") float presionSistolica,
                                           @Query("presionDistolica") float presionDistolica,
                                           @Query("pulso") float pulso,
                                           @Query("peso") float peso,
                                           @Query("tmb") float tmb,
                                           @Query("porcentajeAgua") float porcentajeAgua,
                                           @Query("porcentajeGrasa") float porcentajeGrasa,
                                           @Query("dmo") float dmo,
                                           @Query("masaMuscular") float masaMuscular,
                                           @Query("observacion") String observacion,
                                           @Query("medido") String medido,
                                           @Query("fecha") String fecha
    );

    public class RegCrtPacient{
        int codigo;
        String respuesta;

        String email;
        float glucosa;
        float insulina;
        float hba1c;
        float cetona;
        float estatura;
        float colesterol;
        float trigliceridos;
        float hdl;
        float ldl;
        float presionSistolica;
        float presionDistolica;
        float pulso;
        float peso;
        float tmb;
        float porcentajeAgua;
        float porcentajeGrasa;
        float dmo;
        float masaMuscular;


        float medido;
        String fecha;
        String observacion;

        private int idCodResult;
        private String resultDescription;
        private int idRegisterDB;

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }

        public float getGlucosa() {
            return glucosa;
        }

        public void setGlucosa(float glucosa) {
            this.glucosa = glucosa;
        }

        public float getInsulina() {
            return insulina;
        }

        public void setInsulina(float insulina) {
            this.insulina = insulina;
        }

        public float getHba1c() {
            return hba1c;
        }

        public void setHba1c(float hba1c) {
            this.hba1c = hba1c;
        }

        public float getCetona() {
            return cetona;
        }

        public void setCetona(float cetona) {
            this.cetona = cetona;
        }

        public float getEstatura() {
            return estatura;
        }

        public void setEstatura(float estatura) {
            this.estatura = estatura;
        }

        public float getColesterol() {
            return colesterol;
        }

        public void setColesterol(float colesterol) {
            this.colesterol = colesterol;
        }

        public float getTrigliceridos() {
            return trigliceridos;
        }

        public void setTrigliceridos(float trigliceridos) {
            this.trigliceridos = trigliceridos;
        }

        public float getHdl() {
            return hdl;
        }

        public void setHdl(float hdl) {
            this.hdl = hdl;
        }

        public float getLdl() {
            return ldl;
        }

        public void setLdl(float ldl) {
            this.ldl = ldl;
        }

        public float getPresionSistolica() {
            return presionSistolica;
        }

        public void setPresionSistolica(float presionSistolica) {
            this.presionSistolica = presionSistolica;
        }

        public float getPresionDistolica() {
            return presionDistolica;
        }

        public void setPresionDistolica(float presionDistolica) {
            this.presionDistolica = presionDistolica;
        }

        public float getPulso() {
            return pulso;
        }

        public void setPulso(float pulso) {
            this.pulso = pulso;
        }

        public float getPeso() {
            return peso;
        }

        public void setPeso(float peso) {
            this.peso = peso;
        }

        public float getTmb() {
            return tmb;
        }

        public void setTmb(float tmb) {
            this.tmb = tmb;
        }

        public float getPorcentajeAgua() {
            return porcentajeAgua;
        }

        public void setPorcentajeAgua(float porcentajeAgua) {
            this.porcentajeAgua = porcentajeAgua;
        }

        public float getPorcentajeGrasa() {
            return porcentajeGrasa;
        }

        public void setPorcentajeGrasa(float porcentajeGrasa) {
            this.porcentajeGrasa = porcentajeGrasa;
        }

        public float getDmo() {
            return dmo;
        }

        public void setDmo(float dmo) {
            this.dmo = dmo;
        }

        public float getMasaMuscular() {
            return masaMuscular;
        }

        public void setMasaMuscular(float masaMuscular) {
            this.masaMuscular = masaMuscular;
        }

        public void setMedido(float medido) {
            this.medido = medido;
        }

        public int getIdCodResult() {
            return idCodResult;
        }

        public void setIdCodResult(int idCodResult) {
            this.idCodResult = idCodResult;
        }

        public String getResultDescription() {
            return resultDescription;
        }

        public void setResultDescription(String resultDescription) {
            this.resultDescription = resultDescription;
        }

        public int getIdRegisterDB() {
            return idRegisterDB;
        }

        public void setIdRegisterDB(int idRegisterDB) {
            this.idRegisterDB = idRegisterDB;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

        public float getMedido() {
            return medido;
        }

        public void setMedido(Float medido) {
            this.medido = medido;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }
    }


}
